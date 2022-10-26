package com.socialCircle.service;


import com.socialCircle.entity.Result;
import com.socialCircle.entity.SealNumber;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;

public interface UserService {
    /**
     * 登录
     */
    Result login(User user);

    /**
     * 获取自己的信息
     */
    Result userInfo(User user);

    /**
     * 注册管理
     */
    Result signIn(SignIn signIn);

    /**
     * 修改管理员权限
     */
    Result updateManagerPermission(User user);

    /**
     * 删除管理
     */
    Result deleteManager(Integer id);

    /**
     * 封号
     */
    Boolean banned(SealNumber sealNumber);

    /**
     * 退出登录
     */
    Result logout(User user);
}
