package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.entity.UserInfo;

public interface UserInfoService {

    /**
     * 查询用户
     * @param q 关键词
     * @param p 页码
     * @param user 当前登录对象
     */
    Result getUsers(String q, Integer p, User user);

    /**
     * 按照用户id查询个人信息
     * @param id 用户id
     * @return
     */
    UserInfo getUserInfoByUserId(Integer id);

    void save(SignIn user);
}
