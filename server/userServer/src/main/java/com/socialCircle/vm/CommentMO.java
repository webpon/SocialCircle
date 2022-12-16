package com.socialCircle.vm;

import com.socialCircle.entity.Comment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentMO {
    public CommentMO() {
    }
    public CommentMO(Comment c) {
        id=c.getId();
        parentId=c.getParentId();
        userId=c.getUserId();
        dynamicId=c.getDynamicId();
        likeNum=c.getLikeNum();
        content=c.getContent();
        createTime=c.getCreateTime();
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

    private List<CommentMO> childList;
}
