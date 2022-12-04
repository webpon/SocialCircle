package com.socialCircle.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户兴趣表
 */
@Data
public class UserHobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 兴趣id
     */
    private Integer hobbyId;

    /**
     * 用户id
     */
    private Integer userId;

    private String hobbyName;
}