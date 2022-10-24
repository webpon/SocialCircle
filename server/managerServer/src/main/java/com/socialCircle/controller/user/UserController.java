package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/login")
    public Result login(User user){
        return userService.login(user);
    }
    @RequestMapping("/userInfo")
    public Result userInfo(@RequestAttribute User user){
        return userService.userInfo(user);
    }
}
