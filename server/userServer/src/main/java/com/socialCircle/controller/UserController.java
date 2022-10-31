package com.socialCircle.controller;

import com.google.code.kaptcha.Constants;
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

    @PostMapping("/signIn")
    public Result signIn(@RequestParam SignIn signIn){
        return userService.signIn(signIn);
    }

    @GetMapping("/login")
    public Result login(User user){
        return userService.login(user);
    }

    @GetMapping("/emailCode")
    public Result emailCode(@RequestAttribute(Constants.KAPTCHA_SESSION_KEY) String sessionCode,
                            String code,
                            String email){
        return userService.emailCode(sessionCode, email,code);
    }
}
