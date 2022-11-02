package com.socialCircle.controller;

import com.google.code.kaptcha.Constants;
import com.socialCircle.common.SentSimpleMail;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/signIn")
    public Result signIn(@RequestBody SignIn signIn){
        return userService.signIn(signIn);
    }

    @GetMapping("/login")
    public Result login(User user){
        return userService.login(user);
    }

    @GetMapping("/emailCode")
    public Result emailCode(HttpSession httpSession,
                            String code,
                            String email){
        String s = (String) httpSession.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        return userService.emailCode(s, email,code);
    }
}
