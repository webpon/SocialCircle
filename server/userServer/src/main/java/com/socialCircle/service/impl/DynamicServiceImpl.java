package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.DynamicDao;
import com.socialCircle.entity.*;
import com.socialCircle.service.DynamicService;
import com.socialCircle.service.ImageService;
import com.socialCircle.service.TopicService;
import com.socialCircle.vm.DynamicVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.socialCircle.constant.RedisKey.DYNAMIC_QUERY_KEY;

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

    @Override
    public Result getDynamicsByUserId(Integer p, Integer userId) {
        String key = p.toString()+":"+userId;
        // redis查询对象
        RedisQuery<List<DynamicVM>> query =
                new RedisQuery<>(DYNAMIC_QUERY_KEY + "my:", key, null, DateField.MINUTE, 20);
        RedisCommand redisCommand = (k) -> {
            Page dynamicPage = new Page<>((p - 1) * 10, 10);
            QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            Page page = dynamicDao.selectPage(dynamicPage, queryWrapper);
            List<Dynamic> data = page.getRecords();
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
            RedisQuery<List<DynamicVM>> listRedisQuery =
                    new RedisQuery<>(DYNAMIC_QUERY_KEY + "my:", p.toString(), dynamicVMS, DateField.MINUTE, 20);
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

    @Override
    public Result addDynamic(DynamicVM dynamicVM, User user) {
        Dynamic dynamic = dynamicVM.getDynamic();
        dynamic.setPublishTime(new Date());
        dynamic.setUserId(user.getId());
        if (dynamicDao.insert(dynamic) > 0) {
            List<Image> images = dynamicVM.getImages();
            imageService.saveListByDynamicId(images, dynamic.getId());
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
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }
}
