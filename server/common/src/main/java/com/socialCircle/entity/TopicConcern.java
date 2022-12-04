package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 话题关注表
 */
@Data
public class TopicConcern implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id
     */
    private Integer topicId;

    /**
     * 用户id
     */
    private Integer userId;

}