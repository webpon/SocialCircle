package com.socialCircle.vm;

import lombok.Data;

@Data
public class ClassifyVM {

    private Integer id;

    /**
     * 分类名字
     */
    private String title;

    private Long dynamicCountNumber;
}
