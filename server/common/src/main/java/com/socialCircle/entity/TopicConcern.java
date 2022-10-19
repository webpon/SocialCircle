package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 话题关注表
 */
@Data
public class TopicConcern implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 分类id
     */
    private Integer topicId;

    /**
     * 用户id
     */
    private Integer userId;

}