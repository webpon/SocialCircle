package com.socialCircle.msg;
import com.socialCircle.entity.Like;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 点赞
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LikeMsg extends Message<LikeMsg> {
    public LikeMsg() {
    }
    public LikeMsg(Like like) {
        dynamicId = like.getDynamicId();
        commentId = like.getCommentId();
        userId = like.getUserId();
    }

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer dynamicId;
    private Integer commentId;

    /**
     * 用户id
     */
    private Integer userId;

}