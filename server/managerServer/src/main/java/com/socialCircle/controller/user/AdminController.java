package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 修改管理权限
     */
    @PutMapping
    public Result updateManagerPermission(@RequestBody User user){
        return userService.updateManagerPermission(user);
    }

    @PostMapping
    public Result addManager(@RequestBody SignIn signIn){
        return userService.signIn(signIn);
    }

    @DeleteMapping
    public Result deleteManager(Integer id){
        return userService.deleteManager(id);
    }

    @GetMapping
    public Result getManagers(String q, @RequestParam(defaultValue = "1",required = false) Integer p){
        return userInfoService.getManagers(q,p);
    }
}
