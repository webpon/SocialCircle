package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论表
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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