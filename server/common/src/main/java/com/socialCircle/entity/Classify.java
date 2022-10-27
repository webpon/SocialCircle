package com.socialCircle.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类表
 */
@Data
public class Classify implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 分类名字
     */
    private String title;


}