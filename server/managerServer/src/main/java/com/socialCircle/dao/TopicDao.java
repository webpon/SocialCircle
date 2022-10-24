package com.socialCircle.dao;

import com.socialCircle.entity.Topic;
import org.apache.ibatis.annotations.Param;


public interface TopicDao {
    Topic queryById(@Param("id") Integer id);
}
