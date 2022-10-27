package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;

import java.util.List;


public interface TopicService {

    /**
     * id查询话题
     * @param id id
     */
    Topic queryById(Integer id);

    /**
     * 获取话题信息
     * @param p 页码
     */
    Result getTopic(Integer p);

    /**
     * 删除话题
     * @param ids 话题id
     */
    Result deleteTopic(List<Integer> ids);

    /**
     * 添加话题
     * @param topic 内容
     */
    Result addTopic(Topic topic);
}
