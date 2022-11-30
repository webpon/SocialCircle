package com.socialCircle.controller;

import com.socialCircle.common.JWTUtil;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserService userService;

    @GetMapping
    public Result info(@RequestAttribute("user")User user){
        return userInfoService.userInfo(user.getId(), null, user);
    }

    @PutMapping
    public Result updateUser(@RequestBody SignIn signIn, @RequestAttribute User user){
       if (signIn.getNewPassword() != null){
           return userService.updatePassword(signIn,user);
       }
       return userInfoService.updateUserInfo(signIn, user);
    }

    @GetMapping("/infoByUserId")
    public Result getUserInfoByUserId(Integer userId,
                                      @RequestAttribute User user,
                                      @RequestParam(required = false) List<String> fields){
        return userInfoService.userInfo(userId,fields, user);
    }

}
