package com.socialCircle.vm;

import com.socialCircle.entity.User;
import com.socialCircle.entity.UserInfo;
import com.socialCircle.entity.WorkExperience;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoVM {

    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账号
     */
    private String accountId;

    /**
     * 登录状态
     */
    private int login;

    /**
     * 封号状态
     */
    private int banned;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 权限
     */
    private Integer permission;


    /**
     * 昵称
     */
    private String petName;

    /**
     * 头像
     */
    private String headIcon;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 职业
     */
    private String job;

    /**
     * 地区
     */
    private String region;

    /**
     * 手机号
     */
    private String phone;
}
