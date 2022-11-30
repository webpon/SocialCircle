package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.DynamicDao;
import com.socialCircle.entity.*;
import com.socialCircle.msg.DynamicMsg;
import com.socialCircle.msg.Message;
import com.socialCircle.service.*;
import com.socialCircle.vm.DynamicVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.socialCircle.constant.RedisKey.*;

@Service
public class DynamicServiceImpl implements DynamicService {
    @Resource
    private DynamicDao dynamicDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ImageService imageService;
    @Resource
    private TopicService topicService;
    @Resource
    private FansConcernServer fansConcernServer;
    @Resource
    private FriendServer friendServer;
    @Resource
    private ResponseChatUtil chatUtil;
    @Resource
    private CommentService commentService;
    @Resource
    private LikeService likeService;

    @Override
    public Result getDynamicsByUserId(Integer p, Integer userId) {
        String key = userId + ":" + p;
        // redis查询对象
        RedisQuery<List<DynamicVM>> query =
                new RedisQuery<>(DYNAMIC_QUERY_KEY + "my:", key, null, DateField.MINUTE, 20);
        RedisCommand redisCommand = (k) -> {
            Page dynamicPage = new Page<>((p - 1) * 10, 10);
            QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.orderByDesc("publish_time");
            Page page = dynamicDao.selectPage(dynamicPage, queryWrapper);
            List<Dynamic> data = page.getRecords();
            ArrayList<DynamicVM> dynamicVMS = getDynamicVMS(data);
            RedisQuery<List<DynamicVM>> listRedisQuery =
                    new RedisQuery<>(DYNAMIC_QUERY_KEY + "my:", k, dynamicVMS, DateField.MINUTE, 20);
            redisUtil.save(k, listRedisQuery);
        };
        List<Dynamic> beans = redisUtil.getBeans(query, redisCommand, Dynamic.class);
        if (beans == null) {
            return Result.error("没有数据");
        }
        return Result.ok(beans);
    }

    @Override
    public Result getDynamics(Integer p, Integer classify) {
        String key = p.toString();
        if (classify != null) {
            key += ":" + classify;
        }
        // redis查询对象
        RedisQuery<List<DynamicVM>> query =
                new RedisQuery<>(DYNAMIC_QUERY_KEY, key, null, DateField.MINUTE, 20);
        RedisCommand redisCommand = (k) -> {
            Page dynamicPage = new Page<>((p - 1) * 10, 10);
            QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
            if (classify != null) {
                queryWrapper.eq("classify_id", classify);
            }
            Page page = dynamicDao.selectPage(dynamicPage, queryWrapper);
            List<Dynamic> data = page.getRecords();
            ArrayList<DynamicVM> dynamicVMS = getDynamicVMS(data);
            RedisQuery<List<DynamicVM>> listRedisQuery =
                    new RedisQuery<>(DYNAMIC_QUERY_KEY, p.toString(), dynamicVMS, DateField.MINUTE, 20);
            redisUtil.save(k, listRedisQuery);
        };
        List<Dynamic> beans = redisUtil.getBeans(query, redisCommand, Dynamic.class);
        if (beans == null) {
            return Result.error("没有数据");
        }
        return Result.ok(beans);
    }

    private ArrayList<DynamicVM> getDynamicVMS(List<Dynamic> data) {
        ArrayList<DynamicVM> dynamicVMS = new ArrayList<>();
        data.forEach(dynamic -> {
            DynamicVM dynamicVM = new DynamicVM();
            dynamicVM.setDynamic(dynamic);
            // 获取话题
            if (dynamic.getTopicId() != null) {
                Topic topic = topicService.queryById(dynamic.getTopicId());
                dynamicVM.setTopic(topic);
            }
            // 获取照片
            List<Image> images = imageService.queryByDynamicId(dynamic.getId());
            dynamicVM.setImages(images);
            dynamicVMS.add(dynamicVM);
        });
        return dynamicVMS;
    }

    @Override
    public Result addDynamic(DynamicVM dynamicVM, User user) {
        Dynamic dynamic = dynamicVM.getDynamic();
        dynamic.setPublishTime(new Date());
        dynamic.setUserId(user.getId());
        if (dynamicDao.insert(dynamic) > 0) {
            List<Image> images = dynamicVM.getImages();
            imageService.saveListByDynamicId(images, dynamic.getId());
            ArrayList<Integer> concernList = getConcernList(user.getId());
            concernList.forEach(i -> {
                if (!user.getId().equals(i)) {
                    // 更新通知
                    DynamicMsg dynamicMsg = new DynamicMsg();
                    dynamicMsg.setUserId(user.getId());
                    dynamicMsg.setForm(user.getId());
                    dynamicMsg.setTo(i);
                    Message message = new Message(dynamicMsg);
                    message.setType("dynamic");
                    String key = UUID.randomUUID().toString().substring(10);
                    redisUtil.save(key, message);
                    chatUtil.sendMsg(key);
                }
                redisUtil.batchDelete(DYNAMIC_QUERY_CONCERN_KEY + i);
            });

            return Result.ok(dynamicVM);
        }
        return Result.error("发布失败");
    }

    @Override
    public Result deleteDynamicById(Integer id, User user) {
        Dynamic dynamic = dynamicDao.selectById(id);
        if (!dynamic.getUserId().equals(user.getId())) {
            return Result.error("删除失败");
        }
        if (dynamicDao.deleteById(id) > 0) {
            imageService.deleteByDynamicId(id);
            commentService.deleteCommentByDynamicId(id);
            likeService.deleteByDynamicId(id);
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    @Override
    public Result getDynamicsByConcern(Integer p, Integer id) {
        String key = id.toString() + ":" + p.toString();
        // redis查询对象
        RedisQuery<List<DynamicVM>> query =
                new RedisQuery<>(DYNAMIC_QUERY_CONCERN_KEY, key, null, DateField.MINUTE, 20);
        RedisCommand redisCommand = (k) -> {
            Page dynamicPage = new Page<>((p - 1) * 10, 10);
            QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
            ArrayList<Integer> list = getConcernList(id);
            queryWrapper.in("user_id", list);
            Page page = dynamicDao.selectPage(dynamicPage, queryWrapper);
            List<Dynamic> data = page.getRecords();
            ArrayList<DynamicVM> dynamicVMS = getDynamicVMS(data);
            RedisQuery<List<DynamicVM>> listRedisQuery =
                    new RedisQuery<>(DYNAMIC_QUERY_CONCERN_KEY, k, dynamicVMS, DateField.MINUTE, 20);
            redisUtil.save(k, listRedisQuery);
        };
        List<Dynamic> beans = redisUtil.getBeans(query, redisCommand, Dynamic.class);
        if (beans == null) {
            return Result.error("没有数据");
        }
        return Result.ok(beans);
    }

    @Override
    public Result getDynamicsByTopicId(Integer p, Integer topicId) {
        String key = topicId + ":" + p;
        // redis查询对象
        RedisQuery<List<DynamicVM>> query =
                new RedisQuery<>(DYNAMIC_QUERY_TOPIC_KEY, key, null, DateField.MINUTE, 20);
        RedisCommand redisCommand = (k) -> {
            Page dynamicPage = new Page<>((p - 1) * 10, 10);
            QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("topic_id", topicId);
            Page page = dynamicDao.selectPage(dynamicPage, queryWrapper);

            List<Dynamic> data = page.getRecords();
            ArrayList<DynamicVM> dynamicVMS = getDynamicVMS(data);
            RedisQuery<List<DynamicVM>> listRedisQuery =
                    new RedisQuery<>(DYNAMIC_QUERY_TOPIC_KEY, k, dynamicVMS, DateField.MINUTE, 20);
            redisUtil.save(k, listRedisQuery);
        };
        List<Dynamic> beans = redisUtil.getBeans(query, redisCommand, Dynamic.class);
        if (beans == null) {
            return Result.error("没有数据");
        }
        return Result.ok(beans);
    }

    @Override
    public DynamicVM getDynamicById(Integer dynamicId) {
        Dynamic dynamic = dynamicDao.selectById(dynamicId);
        DynamicVM dynamicVM = new DynamicVM();
        dynamicVM.setDynamic(dynamic);
        return dynamicVM;
    }

    @Override
    public void subtractCommentNum(Integer dynamicId, int num) {
        UpdateWrapper<Dynamic> wrapper = new UpdateWrapper();
        wrapper.eq("id", dynamicId);
        Dynamic dynamic = dynamicDao.selectById(dynamicId);
        dynamic.setCommentNum(dynamic.getCommentNum() - num);
        dynamicDao.update(dynamic, wrapper);
    }

    @Override
    public void addCommentNum(int dynamicId, int num) {
        UpdateWrapper<Dynamic> wrapper = new UpdateWrapper();
        wrapper.eq("id", dynamicId);
        Dynamic dynamic = dynamicDao.selectById(dynamicId);
        dynamic.setCommentNum(dynamic.getCommentNum() + num);
        dynamicDao.update(dynamic, wrapper);
    }

    @Override
    public void subtractLikeNum(Integer dynamicId) {
        UpdateWrapper<Dynamic> wrapper = new UpdateWrapper();
        wrapper.eq("id", dynamicId);
        Dynamic dynamic = dynamicDao.selectById(dynamicId);
        dynamic.setLikeNum(dynamic.getLikeNum() - 1);
        dynamicDao.update(dynamic, wrapper);
    }

    @Override
    public void updateByTopicIds(Integer id) {
        UpdateWrapper<Dynamic> set = new UpdateWrapper<Dynamic>()
                .eq("topic_id", id)
                .set("topic_id", null);
        dynamicDao.update(null, set);
    }

    @Override
    public void addLikeNum(Integer dynamicId) {
        UpdateWrapper<Dynamic> wrapper = new UpdateWrapper();
        wrapper.eq("id", dynamicId);
        Dynamic dynamic = dynamicDao.selectById(dynamicId);
        dynamic.setLikeNum(dynamic.getLikeNum() + 1);
        dynamicDao.update(dynamic, wrapper);
    }

    private ArrayList<Integer> getConcernList(Integer id) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(id);
        /// 获取关注的
        List<FansConcern> fansConcernList = fansConcernServer.getConcernByUserId(id);
        fansConcernList.forEach(fansConcern -> list.add(fansConcern.getConcern()));
        // 获取好友
        List<Friend> friendList = friendServer.getByMeUserId(id);
        friendList.forEach(friend -> list.add(friend.getFriend()));
        return list;
    }
}

