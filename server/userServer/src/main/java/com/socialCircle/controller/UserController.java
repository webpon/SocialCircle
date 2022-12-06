package com.socialCircle.controller;

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
    public Result forgetPassword(@RequestBody SignIn signIn){
        return userService.forgetPassword(signIn);
    }

}
