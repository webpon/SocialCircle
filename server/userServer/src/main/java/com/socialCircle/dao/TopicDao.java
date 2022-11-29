package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.Topic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicDao extends BaseMapper<Topic> {
}
