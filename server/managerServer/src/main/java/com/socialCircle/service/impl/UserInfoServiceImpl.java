package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.RedisQuery;
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
import java.util.concurrent.TimeUnit;

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

    /**
     * 获取管理
     *
     * @param q 关键词
     * @param p 页码
     */
    @Override
    public Result getManagers(String q, Integer p) {
        // 如果没有关键词就开启缓存模式
        if (q == null) {
            List<UserInfoVM> userInfoList = redisUtil.getBeans(RedisKey.MANAGERS_QUERY_KEY + p, UserInfoVM.class);
            // 如果缓存有数据
            if (userInfoList == null) {
                userInfoList = queryUsers(q,p,true);
               redisUtil.save(RedisKey.MANAGERS_QUERY_KEY + p, userInfoList);
            }
            Result<List<UserInfoVM>> ok = Result.ok(userInfoList);
            ok.setTotal(userInfoDao.count(q,p,true));
            return ok;
        }
        List<UserInfoVM> userInfoList = queryUsers(q,p,true);
        Result<List<UserInfoVM>> ok = Result.ok(userInfoList);
        ok.setTotal(userInfoDao.count(q,p,true));
        return ok;
    }

    @Override
    public Result getUsers(String q, Integer p, User user) {
        if (p == null) p = 1;
        // 如果没有关键词就开启缓存模式
        if (q == null) {
            List<UserInfoVM> userInfoList = redisUtil.getBeans(RedisKey.USERS_QUERY_KEY + p, UserInfoVM.class);
            // 如果缓存有数据
            if (userInfoList == null) {
                userInfoList = queryUsers(q,p,false);
                redisUtil.save(RedisKey.USERS_QUERY_KEY+p, userInfoList);
            }
            Result<List<UserInfoVM>> ok = Result.ok(userInfoList);
            ok.setTotal(userInfoDao.count(q,p,false));
            return ok;
        }
        List<UserInfoVM> userInfoList = queryUsers(q, p, false);
        Result<List<UserInfoVM>> ok = Result.ok(userInfoList);
        ok.setTotal(userInfoDao.count(q,p,false));
        return ok;
    }

    @Override
    public UserInfo getUserInfoByUserId(Integer id) {
        String key = "query:user:info:" + id;
        UserInfo bean = redisUtil.getBean(key, UserInfo.class);
        if (bean == null) {
            bean = userInfoDao.getUserInfoByUserId(id);
            redisUtil.save(key, bean);
        }
        return bean;
    }

    @Override
    public void save(SignIn user) {
        userInfoDao.save(user);
    }

    private List<UserInfoVM> queryUsers(String q, Integer p, Boolean getAdmin) {
        p--;
        p *= 10;
        if (q != null){
            q = "%"+q+"%";
        }
        return userInfoDao.queryUsers(q,p,getAdmin);
    }
}
