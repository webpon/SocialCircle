package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;
import com.socialCircle.entity.User;

public interface TopicService {
    Topic queryById(Integer topicId);

    Result getTopic(Integer p);

    Result deleteTopic(Integer id, User user);

    Result addTopic(Topic topic, User user);
}
