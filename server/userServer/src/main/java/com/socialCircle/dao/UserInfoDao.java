package com.socialCircle.dao;

import com.socialCircle.entity.SignIn;

public interface UserInfoDao {
    void save(SignIn user);
}
