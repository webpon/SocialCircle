package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.UserInfo;
import com.socialCircle.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/info")
    public Result getUserInfoByUserId(Integer id){
        UserInfo info = userInfoService.getUserInfoByUserId(id);
        if (info == null) {
            return Result.error();
        }
        return Result.ok(info);
    }
}
