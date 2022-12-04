package com.socialCircle.vm;

import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.UserHobby;
import com.socialCircle.entity.WorkExperience;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    private List<WorkExperience> workExperiences;

    private List<UserHobby> hobbies;

    private Boolean concern;
    private Boolean friend;
    private Boolean fens;
    /**
     * 备注
     */
    private String remarks;
}
