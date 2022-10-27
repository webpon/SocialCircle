package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 点赞
 */
@Data
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer dynamicId;
    private Integer LikeId;

    /**
     * 点赞时间
     */
    private Date createTime;
    /**
     * 用户id
     */
    private Integer userId;

}