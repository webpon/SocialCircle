package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao extends BaseMapper<Comment> {
}
