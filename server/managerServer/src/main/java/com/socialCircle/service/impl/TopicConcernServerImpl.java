package com.socialCircle.service.impl;

import com.socialCircle.dao.TopicConcernDao;
import com.socialCircle.service.TopicConcernServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TopicConcernServerImpl implements TopicConcernServer {
    @Resource
    private TopicConcernDao topicConcernDao;
    /**
     * 按照话题id删除
     *
     * @param ids 话题id
     */
    @Override
    public void deleteByTopicIds(List<Integer> ids) {
        topicConcernDao.deleteByTopicIds(ids);
    }
}
