package com.socialCircle.dao;

import com.socialCircle.entity.SignIn;
import com.socialCircle.vm.UserInfoVM;

public interface UserInfoDao {
    void save(SignIn user);

    UserInfoVM getInfo(Integer id);

    boolean updateUserInfo(SignIn signIn);
}
