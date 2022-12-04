package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.entity.UserInfo;
import com.socialCircle.vm.UserInfoVM;

import java.util.List;

public interface UserInfoService {
    void save(SignIn signIn);

    /**
     * 用户信息
     */
    Result userInfo(Integer user, List<String> fields, User user1);

    /**
     * 修改个人信息
     */
    Result updateUserInfo(SignIn signIn, User user);

    /**
     * 通过模糊查询昵称和id获取基本信息
     * @param q 昵称
     * @param ids id
     * @return
     */
    List<UserInfoVM> queryUserBaseInfoByPetNameAndUserIds(String q, List<Integer> ids);
}
