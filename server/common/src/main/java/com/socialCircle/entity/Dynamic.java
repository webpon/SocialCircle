package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态
 */
@Data
public class Dynamic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

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
//    @TableField(fill = FieldFill.INSERT) //创建时自动填充——
    private Date publishTime;

}