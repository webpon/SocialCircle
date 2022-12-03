package com.socialcircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.Blacklist;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlacklistDao extends BaseMapper<Blacklist> {
}
