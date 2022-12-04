package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.WorkExperienceDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.entity.WorkExperience;
import com.socialCircle.service.WorkExperienceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.socialCircle.constant.RedisKey.WORK_EXPERIENCE;

@Service
public class WorkExperienceServiceImpl implements WorkExperienceService {
    @Resource
    private WorkExperienceDao dao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<WorkExperience> getWorkExperienceByUserId(Integer id) {
        RedisQuery<List<WorkExperience>> query =
                new RedisQuery<>(WORK_EXPERIENCE, id.toString(), null, DateField.DAY_OF_MONTH, 30);
        return redisUtil.getBeans(query, key -> {
            return dao.getWorkExperienceByUserId(id);
        }, WorkExperience.class);
    }

    @Override
    public Result addWorkExperience(User user, WorkExperience workExperience) {
        workExperience.setUserId(user.getId());
        int insert = dao.insert(workExperience);
        if (insert == 1) {
            redisUtil.delete(WORK_EXPERIENCE + user.getId());
            return Result.ok("添加成功", workExperience);
        }
        return Result.error("添加失败");
    }

    @Override
    public Result deleteWorkExperience(Integer id, Integer userId) {
        int delete = dao.delete(new QueryWrapper<WorkExperience>()
                .eq("id", id)
                .eq("user_id", userId));
        if (delete == 0) {
            return Result.error("删除失败");
        }
        redisUtil.delete(WORK_EXPERIENCE + userId);
        return Result.ok("删除成功");
    }
}
