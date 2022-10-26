package com.socialCircle.service.impl;

import com.socialCircle.constant.ResultCode;
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
    private SealNumberDao sealNumberDao;
    @Resource
    private UserService userService;

    @Override
    public Result sealNumber(SealNumber sealNumber) {
        if (sealNumberDao.save(sealNumber) && userService.banned(sealNumber)) {
            return Result.ok("禁用成功");
        }
        return Result.ok("禁用失败");
    }
}
