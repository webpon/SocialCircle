package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/manage/user")
public class ManageUserController {

    @Resource
    private UserInfoService userInfoService;

    @GetMapping
    public Result getUsers(String q, Integer p, @RequestAttribute("user") User user){
        return userInfoService.getUsers(q,p,user);
    }

}
