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
    /**
     * 获取分类
     */
    @Override
    public Result getClassify() {
        List<Classify> beans = redisUtil.getBeans(CLASSIFY, Classify.class);
        if (beans == null) {
            beans = classifyDao.getClassify();
            redisUtil.save(CLASSIFY, beans);
        }
        return Result.ok(beans);
    }

    /**
     * 添加分类
     */
    @Override
    public Result addClassify(Classify classify) {
        if (classifyDao.addClassify(classify)) {
            redisUtil.delete(CLASSIFY);
            return Result.ok(classify);
        }
        return Result.error("添加失败");
    }

    /**
     * 删除分类
     */
    @Override
    public Result deleteClassify(List<Integer> ids) {
        if (classifyDao.deleteById(ids)) {
            redisUtil.delete(CLASSIFY);
            return Result.ok();
        }
        return Result.error("删除失败");
    }
}
