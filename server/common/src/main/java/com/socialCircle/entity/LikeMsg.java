package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 点赞
 */
@Data
public class LikeMsg extends Message<LikeMsg>{
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
     * 点赞时间
     */
    private Date createTime;
    /**
     * 用户id
     */
    private Integer userId;

}