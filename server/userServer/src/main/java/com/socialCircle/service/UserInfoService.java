package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;

import java.util.List;

public interface UserInfoService {
    void save(SignIn signIn);

    /**
     * 用户信息
     * @param user
     */
    Result userInfo(Integer user, List<String> fields);

    /**
     * 修改个人信息
     */
    Result updateUserInfo(SignIn signIn, User user);
}
