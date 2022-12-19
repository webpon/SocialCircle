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
     * 邮箱登录
     * @param email 邮箱
     * @param emailCode 验证码
     */
    Result login(String email, String emailCode);

    /**
     * 忘记密码，修改密码
     * @param signIn
     */
    Result forgetPassword(SignIn signIn);


    /**
     * 修改密码
     */
    Result updatePassword(SignIn signIn, User user);

    void updateBanned(Integer id);
}
