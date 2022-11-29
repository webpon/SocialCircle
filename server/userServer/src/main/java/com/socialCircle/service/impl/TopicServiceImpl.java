package com.socialCircle.service.impl;

import com.socialCircle.dao.TopicDao;
import com.socialCircle.entity.Topic;
import com.socialCircle.service.TopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TopicServiceImpl implements TopicService {
    @Resource
    private TopicDao topicDao;
    @Override
    public Topic queryById(Integer topicId) {
        return topicDao.selectById(topicId);
    }
}
