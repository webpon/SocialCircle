package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片表
 */
@Data
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 举报id
     */
    private Integer reportId;

    /**
     * 动态id
     */
    private Integer dynamicId;

    /**
     * 图片链接
     */
    private String url;

    /**
     * 排序从小到大
     */
    private Integer sort;

}