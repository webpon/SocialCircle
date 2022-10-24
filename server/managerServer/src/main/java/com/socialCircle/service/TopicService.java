package com.socialCircle.service;

import com.socialCircle.entity.Topic;

import java.util.List;

public interface TopicService {

    /**
     * id查询话题
     * @param id id
     */
    Topic queryById(Integer id);
}
