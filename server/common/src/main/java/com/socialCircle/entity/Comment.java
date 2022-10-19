package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论表
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 评论数
     */
    private Long parentId;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer dynamicId;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 分享数
     */
    private String content;

    /**
     * 点赞时间
     */
    private Date createTime;

}