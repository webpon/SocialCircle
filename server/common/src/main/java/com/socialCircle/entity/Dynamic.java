package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态
 */
@Data
public class Dynamic implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 评论数
     */
    private Long commentNum;

    /**
     * 分享数
     */
    private Long shareNum;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer topicId;

    private Integer classifyId;

    /**
     * 发布时间
     */
    private Date publishTime;

}