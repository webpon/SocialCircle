package com.socialCircle.dao;

import com.socialCircle.entity.Topic;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TopicDao {
    Topic queryById(@Param("id") Integer id);

    List<Topic> queryQueryTopics(@Param("p") int p);

    Boolean deleteTopicById(@Param("ids") List<Integer> ids);

    boolean addTopic(Topic topic);
}
