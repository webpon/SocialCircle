package com.socialCircle.controller;

import com.google.code.kaptcha.Constants;
import com.socialCircle.entity.Result;
import com.socialCircle.service.EmailSendService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/code")
public class EmailCodeController {

    @Resource
    private EmailSendService emailSend;

    @GetMapping("/singIn")
    public Result singInEmailCode(HttpSession httpSession,
                                  String code,
                                  String email){
        String s = (String) httpSession.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        return emailSend.singInByEmailCode(s, email,code);
    }

    @GetMapping("/logIn")
    public Result logInEmailCode(HttpSession httpSession,
                                 String code,
                                 String email){
        String s = (String) httpSession.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        return emailSend.logInByEmailCode(s, email,code);
    }


    @GetMapping("/password")
    public Result passwordEmailCode(HttpSession httpSession,
                                 String code,
                                 String email){
        String s = (String) httpSession.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        return emailSend.logInByEmailCode(s, email,code);
    }


}
