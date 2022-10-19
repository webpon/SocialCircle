package com.socialCircle.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 普通消息
 */
@Data
public class PlainMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 发送者
     */
    private Integer sender;

    /**
     * 接收者
     */
    private Integer receiver;

    /**
     * 内容
     */
    private String content;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 发送时间
     */
    private Date sendTime;

}