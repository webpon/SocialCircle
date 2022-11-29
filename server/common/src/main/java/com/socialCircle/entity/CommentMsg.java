package com.socialCircle.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论消息
 */
@Data
public class CommentMsg extends Message {
    public CommentMsg() {
    }

    public CommentMsg(Comment c) {
        id = c.getId();
        parentId = c.getParentId();
        userId = c.getUserId();
        dynamicId = c.getDynamicId();
        likeNum = c.getLikeNum();
        content = c.getContent();
        createTime = c.getCreateTime();
    }

    private Long id;

    /**
     * 评论父id
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