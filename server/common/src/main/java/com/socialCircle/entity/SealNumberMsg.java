package com.socialCircle.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SealNumberMsg extends Message<SealNumberMsg>{
    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 封号理由
     */
    private String reason;

}
