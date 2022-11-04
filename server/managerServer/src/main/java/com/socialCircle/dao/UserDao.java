package com.socialCircle.dao;

import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.vm.UserInfoVM;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    User login(User user);

    UserInfoVM getInfo(@Param("id") Integer id);

    Long getMaxId();

    boolean save(SignIn user);

    User queryByEmail(@Param("email") String email);

    Boolean updateById(User user);

    Boolean deleteManager(@Param("id") Integer id);

    void loginTime(User user);

}
