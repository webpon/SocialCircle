package com.socialCircle.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

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

    public User() {}
}