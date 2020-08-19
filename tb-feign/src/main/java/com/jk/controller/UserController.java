package com.jk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.entity.HobbyEntity;
import com.jk.entity.UserEntity;
import com.jk.service.UserServiceFeign;
import com.jk.utils.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserServiceFeign userService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/saveOrder")
    @ResponseBody
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object saveOrder(Integer userId, Integer productId, HttpServletRequest request){
        return userService.saveOrder(userId,productId);
    }

    //注意，方法签名一定要要和api方法一致 自定义降级方法
    public Object saveOrderFail(Integer userId, Integer productId, HttpServletRequest request){
        System.out.println("controller 保存订单降级方法");


        String sendValue  = (String) redisUtil.get(Constant.SAVE_ORDER_WARNING_KEY);

        String ipAddr = request.getRemoteAddr();
        //新启动一个线程进行业务逻辑处理
        // 开启一个独立线程，进行发送警报，给开发人员，处理问题
        new Thread( ()->{
            if(StringUtil.isEmpty(sendValue)) {
                System.out.println("紧急短信，用户下单失败，请离开查找原因,ip地址是="+ipAddr);

                //发送一个http请求，调用短信服务 TODO
                //写发送短信代码，带有参数发送 userId  productId
                String phoneNumber = "18235915366";
                HashMap<String, Object> result = new HashMap<String, Object>();
                //判断用户是否是在1分钟内操作
                if (redisTemplate.hasKey(Constant.SMS_LOCK+phoneNumber)) {
                    System.out.println("不能在1分钟内重复获取");
                }
                HashMap<String, Object> headers = new HashMap<String, Object>();
                headers.put("AppKey", Constant.APP_KEY);
                String nonce = UUID.randomUUID().toString().replaceAll("-", "");
                headers.put("Nonce", nonce);
                String curtime = System.currentTimeMillis()+"";
                headers.put("CurTime", curtime);
                headers.put("CheckSum", CheckSumBuilder.getCheckSum(Constant.APP_SECRET, nonce, curtime));
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("mobile", phoneNumber);

                params.put("templateid", Constant.TEMPLATEID);
                Integer authCode = (int) Math.floor(Math.random()*8999+10000);

                params.put("authCode", authCode);
                String post = null;
                try {
                    post = HttpClientUtil.post(Constant.SMS_URL, params , headers );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject parseObject = JSON.parseObject(post);
                int code = parseObject.getIntValue("code");
                if (code != 200) {
                    System.out.println("验证码发送失败");
                }
                //将验证码缓存到redis中，有效期5分钟
                redisTemplate.opsForValue().set(Constant.SMS_CODE+phoneNumber,authCode , 5, TimeUnit.MINUTES);
                //将用户发送状态锁定1分钟
                redisTemplate.opsForValue().set(Constant.SMS_LOCK+phoneNumber, "lock", 60, TimeUnit.SECONDS);

                redisUtil.set(Constant.SAVE_ORDER_WARNING_KEY, "用户保存订单失败", 50);
            }else{
                System.out.println("已经发送过短信，1分钟内不重复发送");
            }
        }).start();

        // 反馈给用户看的
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", -1);
        map.put("message", "抢购排队人数过多，请您稍后重试。");

        return map;
    }

    @RequestMapping("/deleteById/{userId}")
    @ResponseBody
    public Boolean deleteById(@PathVariable Integer userId){
        try {
            userService.deleteById(userId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    public UserEntity updateUser(UserEntity user){
        userService.updateUser(user);
        return  user;
    }


    @RequestMapping("/selectById/{userId}")
    public String getUser(@PathVariable Integer userId, Model model){

        UserEntity userEntity = userService.selectById(userId);
        model.addAttribute("info",userEntity);
        return "userEdit";
    }


    @RequestMapping("/toAddPage")
    public String toAddPage(){
        return "userAdd";
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public UserEntity saveUser(UserEntity user){
        userService.saveUser(user);
        return  user;
    }

    @RequestMapping("/typeSelect")
    @ResponseBody
    public List<HobbyEntity> typeSelect(){
        List<HobbyEntity> hobbyList = userService.typeSelect();
        return hobbyList;
    }

    @RequestMapping("toUserPage")
    public String   toUserPage(){
        return "userList";
    }

    @RequestMapping("/selectUserList")
    @ResponseBody
    public List<UserEntity> selectUserList() {
        //获取redis中的值
        List<UserEntity> userList = (List<UserEntity>) redisUtil.get(Constant.SELECT_USER_LIST);

        // 1. 有值   2. 没有值
        if(userList == null || userList.size() <= 0 || userList.isEmpty()) {
            // 从数据库查询，存redis
            userList = userService.selectUserList();
            redisUtil.set(Constant.SELECT_USER_LIST, userList, 30);
        }
       return userList;
    }

}
