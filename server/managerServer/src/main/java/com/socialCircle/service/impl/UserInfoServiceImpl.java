package com.socialCircle.service.impl;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.UserInfoDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.entity.UserInfo;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.vm.UserInfoVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void deleteManager(Integer id) {
       userInfoDao.deleteById(id);
    }

    @Override
    public Result getUsers(String q, Integer p, User user) {
        if (p == null) p = 1;
        // 如果没有关键词就开启缓存模式
        if (q == null) {
            List<UserInfoVM> userInfoList = redisUtil.getBeans(RedisKey.USERS_QUERY_KEY + p, UserInfoVM.class);
            // 如果缓存有数据
            if (userInfoList != null) {
                return Result.ok(userInfoList, ResultCode.NOT_HAVE_DATA);
            }
            userInfoList = queryUsers(q,p,user);
            redisUtil.save(RedisKey.USERS_QUERY_KEY+p, userInfoList);
            return Result.ok(userInfoList, ResultCode.HAVE_DATA);
        }
        List<UserInfoVM> userInfoList = queryUsers(q, p, user);
        return Result.ok(userInfoList, ResultCode.HAVE_DATA);
    }

    @Override
    public UserInfo getUserInfoByUserId(Integer id) {
        return userInfoDao.getUserInfoByUserId(id);
    }

    @Override
    public void save(SignIn user) {
        userInfoDao.save(user);
    }

    private List<UserInfoVM> queryUsers(String q, Integer p, User user) {
        p--;
        p *= 15;
        if (q != null){
            q = "%"+q+"%";
        }
        return userInfoDao.queryUsers(q,p,user);
    }
}
