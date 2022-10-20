package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserService userService;

    @PostMapping
    public Result addManager(@RequestBody SignIn signIn){
        return userService.signIn(signIn);
    }
}
