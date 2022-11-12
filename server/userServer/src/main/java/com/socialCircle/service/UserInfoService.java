package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;

public interface UserInfoService {
    void save(SignIn signIn);

    /**
     * 用户信息
     */
    Result userInfo(User user);

    /**
     * 修改个人信息
     */
    Result updateUserInfo(SignIn signIn, User user);
}
