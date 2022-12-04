package com.socialcircle.server.impl;

import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.entity.Blacklist;
import com.socialcircle.dao.BlacklistDao;
import com.socialcircle.server.BlacklistServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BlacklistServerImpl implements BlacklistServer {
    @Resource
    private BlacklistDao blacklistDao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public Boolean ifHeBlack(Integer me, Integer to) {
        RedisQuery<Blacklist> blacklistRedisQuery = new RedisQuery<>(RedisKey.BLACK_KEY, to + ":" + me, null, DateField.MINUTE, 30);
        RedisCommand<Blacklist> command = (key) -> {
            return blacklistDao.selectOne(new QueryWrapper<Blacklist>()
                    .eq("me", to).eq("black", me)
            );
        };
        Blacklist blacklist = redisUtil.getBean(blacklistRedisQuery, command, Blacklist.class);
        return blacklist != null;
    }

    @Override
    public Boolean ifMeBlack(Integer me, Integer to) {
        RedisQuery<Blacklist> blacklistRedisQuery = new RedisQuery<>(RedisKey.BLACK_KEY, me + ":" + to, null, DateField.MINUTE, 30);
        RedisCommand<Blacklist> command = (key) -> blacklistDao.selectOne(new QueryWrapper<Blacklist>()
                .eq("me", me).eq("black", to)
        );
        Blacklist blacklist = redisUtil.getBean(blacklistRedisQuery, command, Blacklist.class);
        return blacklist != null;
    }
}
