package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 兴趣表
 */
@Data
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 大致分类
     */
    private String title;

    /**
     * 兴趣名称
     */
    private String hobbyName;

    /**
     * 选择的人数
     */
    private Integer personNum;

}