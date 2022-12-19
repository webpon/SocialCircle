package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.SealNumberDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SealNumber;
import com.socialCircle.service.SealNumberService;
import com.socialCircle.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SealNumberServiceImpl implements SealNumberService {

    @Resource
    private SealNumberDao dao;
    @Resource
    private UserService userService;

    @Override
    public Result querySealNumber(Integer id) {
        SealNumber sealNumber = dao.selectOne(new QueryWrapper<SealNumber>()
                .eq("user_id", id)
                .last("and end_time > now()")
        );
        if (sealNumber != null) {
            return Result.ok(sealNumber);
        }
        userService.updateBanned(id);
        dao.delete(new QueryWrapper<SealNumber>()
                .eq("user_id", id)
        );
        return Result.error("已过期");
        }
}
