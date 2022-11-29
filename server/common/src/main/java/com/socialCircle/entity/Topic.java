package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 话题表
 */
@Data
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    @TableField(value = "`describe`")
    private String describe;

    /**
     * 关注数
     */
    private Integer concernNum;

    /**
     * 用户id
     */
    private Integer userId;

}