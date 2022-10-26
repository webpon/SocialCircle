package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.DynamicDao;
import com.socialCircle.entity.Dynamic;
import com.socialCircle.entity.Image;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;
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


    @Override
    public Result getDynamic(Integer p, Integer classify) {
        String key = p.toString();
        if (classify != null) {
            key += ":"+classify;
        }
        // redis查询对象
        RedisQuery<List<DynamicVM>> dynamicVMRedisQuery =
                new RedisQuery<>(DYNAMIC_QUERY_KEY, key, null, DateField.MINUTE, 20);
        // 数据库查询方式
        RedisCommand redisCommand = (k)->{
            Integer p1 = p-1;
            p1 *= 15;
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
            RedisQuery<List<DynamicVM>> listRedisQuery = new RedisQuery<>(DYNAMIC_QUERY_KEY, p.toString(), dynamicVMS, DateField.MINUTE, 20);
            redisUtil.save(k, listRedisQuery, dynamicVMRedisQuery.getOffset()+10, TimeUnit.MINUTES);
        };

        List<DynamicVM> beans = redisUtil.getBeans(dynamicVMRedisQuery, redisCommand, DynamicVM.class);
        if (beans == null) {
            return Result.error("没有更多的数据");
        }
        return Result.ok(beans);
    }

    @Override
    public Result deleteDynamicById(List<Integer> ids) {
        imageService.deleteDynamicById(ids);
        if (dynamicDao.deleteDynamicById(ids)) {
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
                redisUtil.batchDelete(DYNAMIC_QUERY_KEY+"*");
                return Result.error("发布成功");
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
