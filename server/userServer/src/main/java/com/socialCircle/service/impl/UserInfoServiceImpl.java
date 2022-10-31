package com.socialCircle.service.impl;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.dao.UserInfoDao;
import com.socialCircle.entity.SignIn;
import com.socialCircle.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void save(SignIn user) {
        userInfoDao.save(user);
    }

}
