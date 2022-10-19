package com.socialCircle.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 点赞消息
 */
@Data
public class LikeMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 发送者
     */
    private Integer liker;

    /**
     * 动态作者
     */
    private Integer receiver;

    private Integer dynamicId;

    /**
     * 发送时间
     */
    private Date sendTime;

}