package com.socialCircle.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;

    /**
     * 被封号人
     */
    private Integer userId;
    private Integer reportUserId;
    private String content;

}