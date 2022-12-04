package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.Hobby;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HobbyDao extends BaseMapper<Hobby> {
}
