package com.socialCircle.dao;

import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.entity.UserInfo;
import com.socialCircle.vm.UserInfoVM;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoDao {
    /**
     * 查询用户
     * @param q 关键词
     * @param p 页码
     * @param user 当前登录对象
     */
    List<UserInfoVM> queryUsers(@Param("q") String q, @Param("p") Integer p, @Param("user") User user);

    /**
     * 按照用户id查询个人信息
     * @param id 用户id
     */
    UserInfo getUserInfoByUserId(@Param("id") Integer id);

    boolean save(SignIn user);

    /**
     * 根据id删除管理
     */
    void deleteById(@Param("id") Integer id);
}
