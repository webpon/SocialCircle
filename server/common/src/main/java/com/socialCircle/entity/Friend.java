package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 好友表
 */
@Data
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 我的
     */
    private Integer me;

    /**
     * 好友
     */
    private Integer friend;

    /**
     * 备注
     */
    private String remarks;


}