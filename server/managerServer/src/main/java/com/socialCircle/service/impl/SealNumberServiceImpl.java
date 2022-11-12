package com.socialCircle.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.SealNumberDao;
import com.socialCircle.entity.Message;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SealNumber;
import com.socialCircle.entity.SealNumberMsg;
import com.socialCircle.service.ReportService;
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
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ResponseChatUtil chatUtil;

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
        if (!(sealNumberDao.save(sealNumber) && userService.banned(sealNumber))) {
            return Result.error("禁用失败");
        }
        String bean = redisUtil.getBean(RedisKey.LOGIN + sealNumber.getUserId(), String.class);
        // 判断用户是否在线
        if (bean != null){
            SealNumberMsg sealNumberMsg = new SealNumberMsg();
            sealNumberMsg.setTo(sealNumber.getUserId());
            sealNumberMsg.setEndTime(formatDateTime);
            sealNumberMsg.setReason(sealNumber.getReason());
            sealNumberMsg.setForm(1);
            Message message = new Message(sealNumberMsg);
            message.setType("sealNumber");
            String substring = UUID.fastUUID().toString().substring(10);
            redisUtil.save(substring, message);
            chatUtil.sendMsg(substring);
        }
        return Result.ok("禁用成功");
    }
}
