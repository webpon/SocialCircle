package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.TopicConcernDao;
import com.socialCircle.entity.TopicConcern;
import com.socialCircle.service.TopicConcernServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TopicConcernServerImpl implements TopicConcernServer {

    @Resource
    private TopicConcernDao dao;

    @Override
    public void deleteByTopicIds(Integer id) {
        dao.delete(new QueryWrapper<TopicConcern>()
                .eq("topic_id", id));
    }
}
