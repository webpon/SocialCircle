package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 粉丝和关注表
 */
@Data
public class FansConcern implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 我的
     */
    private Integer me;

    private Integer concern;

    /**
     * 备注
     */
    private String remarks;

}