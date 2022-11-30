package com.socialCircle.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConcernMsg extends Message{
    private Integer userId;
}
