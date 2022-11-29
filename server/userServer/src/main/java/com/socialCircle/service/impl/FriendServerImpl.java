package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.FriendDao;
import com.socialCircle.entity.Friend;
import com.socialCircle.service.FriendServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FriendServerImpl implements FriendServer {

    @Resource
    private FriendDao friendDao;

    @Override
    public List<Friend> getByMeUserId(Integer id) {
        return friendDao.selectList(new QueryWrapper<Friend>().eq("me",id));
    }
}
