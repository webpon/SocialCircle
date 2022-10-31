package com.socialCircle.controller.user;

import com.google.code.kaptcha.Constants;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/loginByEmail")
    public Result loginByEmail(String email, String code){
        return userService.loginByEmail(email,code);
    }
    @RequestMapping("/login")
    public Result login(User user){
        return userService.login(user);
    }
    @RequestMapping("/userInfo")
    public Result userInfo(@RequestAttribute User user){
        return userService.userInfo(user);
    }
    @RequestMapping("/logout")
    public Result logout(@RequestAttribute User user){
        return userService.logout(user);
    }
    @RequestMapping("/emailCode")
    public Result emailCode(HttpSession httpSession,
                            String code,
                            String email){
        String s = (String) httpSession.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        return userService.emailCode(s, email,code);
    }

}
