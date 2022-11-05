package com.socialCircle.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.SealNumberDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SealNumber;
import com.socialCircle.service.SealNumberService;
import com.socialCircle.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SealNumberServiceImpl implements SealNumberService {
    @Resource
    private SealNumberDao sealNumberDao;
    @Resource
    private UserService userService;

    @Override
    public Result sealNumber(SealNumber sealNumber) {
        String endTime = sealNumber.getEndTime();
        if (endTime == null) {
            return Result.error("请填写结束时间");
        }
        int i = Integer.parseInt(endTime);
        DateTime dateTime = DateUtil.offsetDay(new Date(), i);
        String formatDateTime = DateUtil.formatDateTime(dateTime);
        sealNumber.setEndTime(formatDateTime);
        if (sealNumberDao.save(sealNumber) && userService.banned(sealNumber)) {
            return Result.ok("禁用成功");
        }
        return Result.error("禁用失败");
    }
}
