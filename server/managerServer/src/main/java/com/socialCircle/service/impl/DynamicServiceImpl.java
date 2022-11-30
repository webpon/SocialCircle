package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.lang.UUID;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.DynamicDao;
import com.socialCircle.entity.*;
import com.socialCircle.msg.ChatMsg;
import com.socialCircle.msg.Message;
import com.socialCircle.service.DynamicService;
import com.socialCircle.service.ImageService;
import com.socialCircle.service.TopicService;
import com.socialCircle.vm.DynamicVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.socialCircle.constant.RedisKey.DYNAMIC_QUERY_KEY;

@Service
public class DynamicServiceImpl implements DynamicService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private DynamicDao dynamicDao;
    @Resource
    private ImageService imageService;
    @Resource
    private TopicService topicService;
    @Resource
    private ResponseChatUtil chatUtil;


    @Override
    public Result getDynamic(Integer p, Integer classify) {
        String key = p.toString();
        if (classify != null) {
            key += ":" + classify;
        }
        // redis查询对象
        RedisQuery<List<DynamicVM>> dynamicVMRedisQuery =
                new RedisQuery<>(DYNAMIC_QUERY_KEY, key, null, DateField.MINUTE, 20);
        // 数据库查询方式
        Integer finalP = p;
        RedisCommand redisCommand = (k) -> {
            Integer p1 = finalP - 1;
            p1 *= 10;
            List<Dynamic> dynamics = dynamicDao.query(p1, classify);
            ArrayList<DynamicVM> dynamicVMS = new ArrayList<>();
            dynamics.forEach(dynamic -> {
                List<Image> images = imageService.queryByDynamicId(dynamic.getId());
                Topic topic = topicService.queryById(dynamic.getTopicId());
                DynamicVM dynamicVM = new DynamicVM();
                dynamicVM.setTopic(topic);
                dynamicVM.setDynamic(dynamic);
                dynamicVM.setImages(images);
                dynamicVMS.add(dynamicVM);
            });
            RedisQuery<List<DynamicVM>> listRedisQuery = new RedisQuery<>(DYNAMIC_QUERY_KEY, finalP.toString(), dynamicVMS, DateField.MINUTE, 20);
            redisUtil.save(k, listRedisQuery, dynamicVMRedisQuery.getOffset() + 10, TimeUnit.MINUTES);
        };

        List<DynamicVM> beans = redisUtil.getBeans(dynamicVMRedisQuery, redisCommand, DynamicVM.class);
        if (beans == null) {
            return Result.error("没有更多的数据");
        }
        p--;
        p *= 10;
        Result<List<DynamicVM>> ok = Result.ok(beans);
        ok.setTotal(dynamicDao.count(classify));
        return ok;
    }

    @Override
    public Result deleteDynamicById(List<Integer> ids) {
        List<Dynamic> dynamicById = dynamicDao.getDynamicById(ids);
        imageService.deleteDynamicById(ids);
        if (dynamicDao.deleteDynamicById(ids)) {
            redisUtil.batchDelete(DYNAMIC_QUERY_KEY);
            dynamicById.forEach(item -> {
                if (item.getUserId() != 1){
                    ChatMsg chatMsg = new ChatMsg();
                    chatMsg.setTo(item.getUserId());
                    chatMsg.setContent("你动态涉嫌违规已被删除");
                    chatMsg.setForm(1);
                    Message message = new Message(chatMsg);
                    message.setType("chat");
                    String key = UUID.randomUUID().toString().substring(10);
                    redisUtil.save(key, message);
                    chatUtil.sendMsg(key);
                }
            });
            return Result.ok("删除成功");
        }
        return Result.ok("删除失败");
    }

    @Override
    public Result addDynamicById(DynamicVM dynamicVM) {
        Dynamic dynamic = dynamicVM.getDynamic();
        dynamic.setUserId(1);
        if (dynamicDao.save(dynamic)) {
            List<Image> images = dynamicVM.getImages();
            images.forEach(image -> {
                // 设置动态id
                image.setDynamicId(dynamic.getId());
            });
            if (imageService.save(images)) {
                redisUtil.batchDelete(DYNAMIC_QUERY_KEY);
                return Result.ok("发布成功");
            }
            ArrayList<Integer> integers = new ArrayList<>();
            integers.add(dynamic.getId());
            dynamicDao.deleteDynamicById(integers);
        }
        return Result.error("发布失败");
    }

    /**
     * 修改动态话题
     *
     * @param ids
     */
    @Override
    public void updateByTopicIds(List<Integer> ids) {
        dynamicDao.updateByTopicIds(ids);
    }
}
