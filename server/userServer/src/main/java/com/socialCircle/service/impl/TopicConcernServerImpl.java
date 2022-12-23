package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.TopicConcernDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;
import com.socialCircle.entity.TopicConcern;
import com.socialCircle.entity.User;
import com.socialCircle.msg.Message;
import com.socialCircle.msg.TopicConcernMsg;
import com.socialCircle.service.TopicConcernServer;
import com.socialCircle.service.TopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TopicConcernServerImpl implements TopicConcernServer {

    @Resource
    private TopicConcernDao dao;
    @Resource
    private TopicService topicService;
    @Resource
    private ResponseChatUtil chatUtil;
    @Resource
    private RedisUtil redisUtil;

    private final String TOPIC_CONCERN = "query:topic:concern:";

    @Override
    public void deleteByTopicIds(Integer id) {
        dao.delete(new QueryWrapper<TopicConcern>()
                .eq("topic_id", id));
    }

    @Override
    public Result concernTopic(TopicConcern topicConcern, User user) {
        topicConcern.setUserId(user.getId());
        redisUtil.batchDelete(TOPIC_CONCERN+ user.getId());
        Topic topic = topicService.queryById(topicConcern.getTopicId());
        if (topic.getUserId().equals(user.getId())) {
            return Result.error("这是你的话题，已关注");
        }
        TopicConcern concern = dao.selectOne(new QueryWrapper<TopicConcern>()
                .eq("user_id", user.getId())
                .eq("topic_id", topicConcern.getTopicId())
        );

        if (concern != null) {
            // 已关注
            if (dao.deleteById(concern.getId()) == 1) {
                // 减少关注数
                topicService.updateConcernNum(concern.getTopicId(), -1);
                redisUtil.batchDelete(TOPIC_CONCERN + user.getId());
                return Result.ok("取消关注成功");
            }
            return Result.error("取消关注失败");
        }
        // 关注
        if (dao.insert(topicConcern) != 1) {
            return Result.error("关注失败");
        }
        redisUtil.batchDelete(TOPIC_CONCERN + user.getId());
        topicService.updateConcernNum(topicConcern.getTopicId(), 1);
        // 发送关注
        TopicConcernMsg topicConcernMsg = new TopicConcernMsg();
        topicConcernMsg.setUserId(user.getId());
        topicConcernMsg.setForm(1);
        topicConcernMsg.setTo(topicConcern.getUserId());
        Message message = new Message(topicConcernMsg);
        chatUtil.sendMsg(message);
        return Result.ok("关注成功");
    }

    @Override
    public Result getConcernTopic(Integer p, User user) {
        RedisQuery<List<TopicConcern>> listRedisQuery =
                new RedisQuery< >(TOPIC_CONCERN, user.getId()+":"+p, null, DateField.MINUTE, 90);
        RedisCommand<List<TopicConcern>> command = (key) -> {
            QueryWrapper<TopicConcern> wrapper = new QueryWrapper<TopicConcern>()
                    .eq("user_id", user.getId())
                    .last("limit " + (p - 1) * 10 + "," + 10);
            return dao.selectList(wrapper);
        };
        List<TopicConcern> beans = redisUtil.getBeans(listRedisQuery, command, TopicConcern.class);
        if (beans == null) {
            return Result.error("没有数据");
        }
        return Result.ok(beans);
    }
}
