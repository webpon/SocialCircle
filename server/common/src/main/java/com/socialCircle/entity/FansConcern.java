package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 粉丝和关注表
 */
@Data
public class FansConcern implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 我的
     */
    private Integer me;

    private Integer concern;

    /**
     * 备注
     */
    private String remarks;

}