package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

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


    private Integer userId;

}
