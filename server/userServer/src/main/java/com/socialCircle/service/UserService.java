package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;

public interface UserService {

    /**
     * 登录
     */
    Result login(User user);

    /**
     * 注册
     */
    Result signIn(SignIn signIn);

    /**
     * 发送验证码
     * @param sessionCode 正确验证码
     * @param email 用户邮箱
     * @param code 用户输入的验证码
     */
    Result emailCode(String sessionCode, String email, String code);
}
