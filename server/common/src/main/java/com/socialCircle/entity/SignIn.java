package com.socialCircle.entity;

import lombok.Data;

@Data
public class SignIn {

    private Integer id;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    private Integer permission;
    private String accountId;

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
    private String newPassword;

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

    private String emailCode;
    private String birthday;
}