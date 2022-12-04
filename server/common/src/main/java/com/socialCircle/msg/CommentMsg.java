package com.socialCircle.msg;

import com.socialCircle.entity.Comment;
import com.socialCircle.msg.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 评论消息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentMsg extends Message {
    public CommentMsg() {
    }

    public CommentMsg(Comment c) {
        id = c.getId();
        parentId = c.getParentId();
        userId = c.getUserId();
        dynamicId = c.getDynamicId();
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

}