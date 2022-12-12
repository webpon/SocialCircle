package com.socialCircle.service.impl;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.dao.ClassifyDao;
import com.socialCircle.entity.Classify;
import com.socialCircle.entity.Result;
import com.socialCircle.service.ClassifyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.socialCircle.constant.RedisKey.CLASSIFY;

@Service
public class ClassifyServiceImpl implements ClassifyService {

    @Resource
    private ClassifyDao classifyDao;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public Result getClassifies() {
        List<Classify> beans = redisUtil.getBeans(CLASSIFY, Classify.class);
        if (beans == null) {
            beans = classifyDao.selectList(null);
            redisUtil.save(CLASSIFY, beans);
        }
        return Result.ok(beans);
    }
}
