package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.UserInfo;
import com.socialCircle.vm.UserInfoVM;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
    void save(SignIn user);

    UserInfoVM getInfo(Integer id);

    boolean updateUserInfo(SignIn signIn);
}
