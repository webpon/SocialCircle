package com.socialCircle.entity;

import lombok.Data;

import java.util.List;

@Data
public class DynamicMsg extends Message<DynamicMsg>{
    /**
     * 发布人的id
     */
    private Integer userId;
    private List<DynamicMsg> list;
}
