package com.socialCircle.service;

import com.socialCircle.entity.Result;

public interface EmailSendService {

    /**
     * 注册发送验证码
     * @param sessionCode 正确验证码
     * @param email 用户邮箱
     * @param code 用户输入的验证码
     */
    Result singInByEmailCode(String sessionCode, String email, String code);
    /**
     * 登录发送验证码
     * @param sessionCode 正确验证码
     * @param email 用户邮箱
     * @param code 用户输入的验证码
     */
    Result  logInByEmailCode(String sessionCode, String email, String code);
}
