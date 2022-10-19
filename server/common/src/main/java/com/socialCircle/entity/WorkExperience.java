package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作经历表
 */
@Data
public class WorkExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 入职时间
     */
    private Date startName;

    /**
     * 离职时间
     */
    private Date endTime;

    /**
     * 所在的行业
     */
    private String industry;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 用户id
     */
    private Integer userId;

}