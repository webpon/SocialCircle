package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 点赞
 */
@Data
@TableName(value = "`like`")
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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