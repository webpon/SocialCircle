package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.Like;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeDao extends BaseMapper<Like> {
}
