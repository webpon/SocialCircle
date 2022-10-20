package com.socialCircle.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SealNumber implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;

    /**
     * 被封号人
     */
    private Integer userId;

    /**
     * 封号时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 封号理由
     */
    private String reason;
}