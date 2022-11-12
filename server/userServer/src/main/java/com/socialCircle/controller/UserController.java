package com.socialCircle.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.google.code.kaptcha.Constants;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.common.SentSimpleMail;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private ResponseChatUtil responseChatUtil;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/test")
    public void test(){
    }
    @PostMapping("/signIn")
    public Result signIn(@RequestBody SignIn signIn){
        return userService.signIn(signIn);
    }

    @GetMapping("/login")
    public Result login(User user){
        return userService.login(user);
    }

    @GetMapping("/loginByEmail")
    public Result login(String email, String emailCode){
        return userService.login(email,emailCode);
    }
    @PutMapping("/forget")
    public Result forgetPassword(@RequestBody SignIn signIn,
                                 @RequestAttribute User user){
        return userService.forgetPassword(signIn, user);
    }

}
