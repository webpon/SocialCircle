package com.socialCircle.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论消息
 */
@Data
public class CommentMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 评论者
     */
    private Integer commenter;

    /**
     * 动态作者
     */
    private Integer receiver;

    private Long commentId;

    private Integer dynamicId;

    /**
     * 发送时间
     */
    private Date sendTime;

}