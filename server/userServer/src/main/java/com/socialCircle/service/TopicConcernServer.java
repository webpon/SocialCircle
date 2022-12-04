package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.TopicConcern;
import com.socialCircle.entity.User;

public interface TopicConcernServer {
    void deleteByTopicIds(Integer id);

    /**
     * 关注话题
     */
    Result concernTopic(TopicConcern topicConcern, User user);

    /**
     * 获取关注
     */
    Result getConcernTopic(Integer p, User user);
}
