package com.socialCircle.service;

import java.util.List;

public interface TopicConcernServer {
    /**
     * 按照话题id删除
     * @param ids 话题id
     */
    void deleteByTopicIds(List<Integer> ids);
}
