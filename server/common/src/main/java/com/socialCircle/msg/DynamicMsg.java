package com.socialCircle.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DynamicMsg extends Message<DynamicMsg> {
    /**
     * 发布人的id
     */
    private Integer userId;

}
