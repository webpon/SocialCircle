package com.socialCircle.dao;

import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.vm.UserInfoVM;

public interface UserDao {
    User login(User user);

    User queryByEmail(String email);

    Long getMaxId();

    boolean save(SignIn signIn);

//    UserInfoVM getInfo(Integer id);
}
