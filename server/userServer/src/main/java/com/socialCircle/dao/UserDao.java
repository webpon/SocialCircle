package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.vm.UserInfoVM;

public interface UserDao extends BaseMapper<User> {
    User login(User user);

    User queryByEmail(String email);

    Long getMaxId();

    boolean save(SignIn signIn);

    void loginTime(User login);

    UserInfoVM getInfo(Integer id);

    boolean updatePassword(SignIn signIn);

//    UserInfoVM getInfo(Integer id);
}
