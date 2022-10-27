package com.socialCircle.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopicConcernDao {
    void deleteByTopicIds(@Param("ids") List<Integer> ids);
}
