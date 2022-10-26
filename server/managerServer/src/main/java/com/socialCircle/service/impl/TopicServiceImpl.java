package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.TopicDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;
import com.socialCircle.entity.TopicConcern;
import com.socialCircle.service.DynamicService;
import com.socialCircle.service.TopicConcernServer;
import com.socialCircle.service.TopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class TopicServiceImpl implements TopicService {

    @Resource
    private TopicDao topicDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private DynamicService dynamicService;
    @Resource
    private TopicConcernServer topicConcernServer;

    @Override
    public Topic queryById(Integer id) {
        return topicDao.queryById(id);
    }

    /**
     * 获取话题信息
     *
     * @param p 页码
     */
    @Override
    public Result getTopic(Integer p) {
        RedisQuery<List<Topic>> listRedisQuery =
                new RedisQuery<>(RedisKey.TOPIC_QUERY_KEY, p.toString(), null, DateField.MINUTE, 30);
        RedisCommand redisCommand = (key) -> {
            int integer = p - 1;
            integer *= 15;
            List<Topic> topics = topicDao.queryQueryTopics(integer);
            listRedisQuery.setData(topics);
            redisUtil.save(key, listRedisQuery, 30, TimeUnit.MINUTES);
        };
        List<Topic> beans = redisUtil.getBeans(listRedisQuery, redisCommand, Topic.class);
        if (beans != null) {
            return Result.ok(beans);
        }
        return Result.error("没有数据");
    }

    /**
     * 删除话题
     *
     * @param ids 话题id
     */
    @Override
    public Result deleteTopic(List<Integer> ids) {
        dynamicService.updateByTopicIds(ids);
        topicConcernServer.deleteByTopicIds(ids);
        if (topicDao.deleteTopicById(ids)) {
            threadPoolExecutor.execute(()-> {
                redisUtil.delete(RedisKey.TOPIC_QUERY_KEY+1);
                getTopic(1);
            });
            return Result.ok("删除成功");
        }
        return Result.ok("删除成功");
    }

    /**
     * 添加话题
     *
     * @param topic 内容
     */
    @Override
    public Result addTopic(Topic topic) {
        if (topicDao.addTopic(topic)){
            threadPoolExecutor.execute(()-> {
                redisUtil.delete(RedisKey.TOPIC_QUERY_KEY+1);
                getTopic(1);
            });
            return Result.ok("添加成功",topic);
        }
        return Result.ok("添加失败",topic);
    }
}
