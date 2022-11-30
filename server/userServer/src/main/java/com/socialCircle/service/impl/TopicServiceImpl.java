package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.TopicDao;
import com.socialCircle.entity.Dynamic;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;
import com.socialCircle.entity.User;
import com.socialCircle.service.DynamicService;
import com.socialCircle.service.TopicConcernServer;
import com.socialCircle.service.TopicService;
import net.sf.jsqlparser.statement.select.Top;
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
    private DynamicService dynamicService;
    @Resource
    private TopicConcernServer topicConcernServer;

    @Override
    public Topic queryById(Integer topicId) {
        return topicDao.selectById(topicId);
    }

    @Override
    public Result getTopic(Integer p) {
        RedisQuery<List<Topic>> listRedisQuery =
                new RedisQuery<>(RedisKey.TOPIC_QUERY_KEY, p.toString(), null, DateField.MINUTE, 30);
        RedisCommand<List<Topic>> redisCommand = (key) -> {
            QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
            return topicDao.selectList(topicQueryWrapper.last("limit " + (p - 1) * 10 + "," + 10));
        };
        List<Topic> beans = redisUtil.getBeans(listRedisQuery, redisCommand, Topic.class);
        if (beans != null) {
            return Result.ok(beans);
        }
        return Result.error("没有数据");
    }

    @Override
    public Result deleteTopic(Integer id, User user) {
        Topic topic = topicDao.selectById(id);
        if (topic == null) {
            return Result.error("没有这话题");
        }
        if (!topic.getUserId().equals(user.getId())) {
            return Result.error("此话题不是你创建");
        }
        dynamicService.updateByTopicIds(id);
        topicConcernServer.deleteByTopicIds(id);
        if (topicDao.deleteById(id) == 1) {
            redisUtil.delete(RedisKey.TOPIC_QUERY_KEY + 1);
            return Result.ok("删除成功");
        }
        return Result.ok("删除成功");
    }

    @Override
    public Result addTopic(Topic topic, User user) {
        topic.setUserId(user.getId());
        if (topicDao.insert(topic) == 1) {
            redisUtil.delete(RedisKey.TOPIC_QUERY_KEY + 1);
            return Result.ok("添加成功", topic);
        }
        return Result.ok("添加失败", topic);
    }

    @Override
    public Result getTopic(Integer p, User user) {
        RedisQuery<List<Topic>> listRedisQuery =
                new RedisQuery<>(RedisKey.TOPIC_QUERY_KEY, p + ":" + user.getId(),
                        null, DateField.MINUTE, 30);
        RedisCommand<List<Topic>> redisCommand = (key) -> {
            QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
            return topicDao.selectList(topicQueryWrapper
                    .eq("user_id", user.getId())
                    .last("limit " + (p - 1) * 10 + "," + 10));
        };
        List<Topic> beans = redisUtil.getBeans(listRedisQuery, redisCommand, Topic.class);
        if (beans != null) {
            return Result.ok(beans);
        }
        return Result.error("没有数据");
    }

    @Override
    public void updateConcernNum(Integer topicId, Integer num) {
        UpdateWrapper<Topic> wrapper = new UpdateWrapper();
        wrapper.eq("id", topicId);
        Topic topic = topicDao.selectById(topicId);
        topic.setConcernNum(topic.getConcernNum() + num);
        topicDao.update(topic, wrapper);
    }

}
