package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.dao.FriendDao;
import com.socialCircle.entity.*;
import com.socialCircle.msg.ChatMsg;
import com.socialCircle.msg.ConcernMsg;
import com.socialCircle.msg.Message;
import com.socialCircle.service.FansConcernServer;
import com.socialCircle.service.FriendServer;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.UserService;
import com.socialCircle.vm.UserInfoVM;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j
public class FriendServerImpl implements FriendServer {

    @Resource
    private FriendDao friendDao;
    @Resource
    private FansConcernServer fansConcernServer;
    @Resource
    private ResponseChatUtil chatUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserInfoService userInfoService;

    @Override
    public List<Friend> getByMeUserId(Integer id) {
        return friendDao.selectList(new QueryWrapper<Friend>().eq("me", id));
    }

    /**
     * 关注
     *
     * @param fansConcern
     * @param user
     */
    @Override
    public Result concern(FansConcern fansConcern, User user) {
        Integer concernUserId = fansConcern.getConcern();
        Integer userId = user.getId();
        Result result = userInfoService.userInfo(concernUserId, Collections.singletonList(""), user);
        if (log.isDebugEnabled()) {
            log.debug("userId:"+userId+", concernUserId:"+ concernUserId);
        }
        if (result.getCode() == 400) {
            return Result.error("关注失败");
        }
        // 获取锁
        if (redisUtil.getLock("concern:" + userId + ":" + concernUserId)) {
            if (log.isDebugEnabled()) {
                log.debug("获取锁成功");
            }
            // 设置锁
            if (!redisUtil.lock("concern:" + concernUserId + ":" + userId, 5, TimeUnit.SECONDS)) {
                if (log.isDebugEnabled()) {
                    log.debug("设置锁失败");
                }
                return Result.error("关注失败");
            }
            // 执行业务
            Result<Object> concern = concern(fansConcern, concernUserId, userId);
            // 删除锁
            redisUtil.unLock("concern:" + concernUserId + ":" + userId);
            return concern;
        }
        return Result.error("关注失败");
    }

    private Result<Object> concern(FansConcern fansConcern, Integer concernUserId, Integer userId) {
        // 是否关注了
        if (fansConcernServer.meConcernHeByUserId(userId, concernUserId) != null) {
            // 取消关注
            if (fansConcernServer.unConcern(userId, concernUserId)) {
                // 通知对方
                send(concernUserId, userId, 2);
                return Result.ok("取消关注成功");
            }
            return Result.error("取消关注失败");
        }
        // 是否是好友
        if (heIsFriend(concernUserId, userId) != null) {
            if (log.isDebugEnabled()) {
                log.debug("取消关注");
            }
            // 是
            int delete = friendDao.delete(new QueryWrapper<Friend>()
                    .eq("me", userId)
                    .eq("friend", concernUserId));
            int delete1 = friendDao.delete(new QueryWrapper<Friend>()
                    .eq("me", concernUserId)
                    .eq("friend", userId));
            fansConcern.setMe(concernUserId);
            fansConcern.setConcern(userId);
            fansConcernServer.Concern(fansConcern);
            if (delete+delete1 > 1) {
                send(concernUserId, userId, 2);
                return Result.ok("取消关注成功");
            }
            return Result.error("取消关注失败");
        }
        // 互加好友
        FansConcern oneFans = fansConcernServer.getOneFans(userId, concernUserId);
        if (oneFans != null) {
            if (log.isDebugEnabled()) {
                log.debug("互加好友");
            }
            return makeFriends(concernUserId, userId, oneFans);
        }
        // 关注
        fansConcern.setMe(userId);
        if (fansConcernServer.Concern(fansConcern)) {
            send(concernUserId, userId, 1);
            return Result.ok("关注成功");
        }
        return Result.error("关注失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<Object> makeFriends(Integer concernUserId, Integer userId, FansConcern oneFans) {
        if (!fansConcernServer.unConcern(concernUserId, userId)) {
            return Result.error("回关失败");
        }
        // 创建我的好友
        Friend meFriend = new Friend();
        meFriend.setFriend(concernUserId);
        meFriend.setMe(userId);
        // 创建他的好友
        Friend heFriend = new Friend();
        heFriend.setMe(concernUserId);
        heFriend.setFriend(userId);
        heFriend.setRemarks(oneFans.getRemarks());
        // 保存
        int insert1 = friendDao.insert(meFriend);
        int insert = friendDao.insert(heFriend);
        if (insert > 0 && insert1 > 0) {
            try {
                // 发送回关消息
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setContent("我们已成为好友一起聊天吧!");
                chatMsg.setType("1");
                Message message = new Message(chatMsg);
                message.setTo(concernUserId);
                message.setForm(userId);
                message.setType("chat");
                chatUtil.sendMsg(message);
                return Result.ok("回关成功");
            } catch (Exception e) {
                log.error(e);
            }
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        throw new RuntimeException();
    }

    private void send(Integer concernUserId, Integer userId, Integer type) {
        try {
            ConcernMsg concernMsg = new ConcernMsg();
            concernMsg.setUserId(userId);
            concernMsg.setType(type.toString());
            Message message = new Message(concernMsg);
            message.setType("concern");
            message.setTo(concernUserId);
            chatUtil.sendMsg(message);
        } catch (Exception e) {

        }
    }

    /**
     * 他是不是你的好友
     *
     * @param heId   他
     * @param userId 自己
     */
    @Override
    public Friend heIsFriend(Integer heId, Integer userId) {
        Friend friend = friendDao.selectOne(new QueryWrapper<Friend>()
                .eq("me", userId)
                .eq("friend", heId));
        return friend;
    }

    /**
     * 获取好友
     *
     * @param q    搜索昵称
     * @param p    页
     * @param user 自己
     */
    @Override
    public Result getFriend(String q, Integer p, User user) {
        if (q != null) {
            // 搜索功能
            List<Friend> friendList = friendDao.selectList(new QueryWrapper<Friend>()
                    .select("id")
                    .eq("me", user.getId())
            );

            if (friendList.isEmpty()) {
                return Result.error("没有数据");
            }
            List<Integer> ids = friendList.stream().map(Friend::getFriend).collect(Collectors.toList());
            List<UserInfoVM> userInfoVMS = userInfoService.queryUserBaseInfoByPetNameAndUserIds(q, ids);
            return Result.ok(userInfoVMS);
        }
        // 分页查询
        List<Friend> friendList = friendDao.selectList(new QueryWrapper<Friend>()
                .eq("me", user.getId()).last("limit "+(p - 1) * 10+","+10));
        ArrayList<UserInfoVM> list = new ArrayList<>();
        for (Friend friend : friendList) {
            Result result = userInfoService.userInfo(friend.getFriend(),
                    Collections.singletonList(""), user);
            UserInfoVM data = (UserInfoVM) result.getData();
            list.add(data);
        }

        return Result.ok(list);
    }

    /**
     * 获取关注列表
     *
     * @param q    搜索昵称
     * @param p    页
     * @param user 自己
     */
    @Override
    public Result getConcern(String q, Integer p, User user) {
        if (q != null) {
            // 搜索功能
            List<FansConcern> concerns = fansConcernServer.getConcernByUserId(user.getId());
            if (concerns.isEmpty()) {
                return Result.error("没有数据");
            }
            List<Integer> ids = concerns.stream().map(FansConcern::getConcern).collect(Collectors.toList());
            List<UserInfoVM> userInfoVMS = userInfoService.queryUserBaseInfoByPetNameAndUserIds(q, ids);
            return Result.ok(userInfoVMS);
        }
        List<FansConcern> concerns = fansConcernServer.getConcernByUserId(user.getId(),p);
        if (concerns.isEmpty()) {
            return Result.error("没有数据");
        }
        ArrayList<UserInfoVM> list = new ArrayList<>();
        for (FansConcern fansConcern : concerns) {
            Result result = userInfoService.userInfo(fansConcern.getConcern(),
                    Collections.singletonList(""), user);
            UserInfoVM data = (UserInfoVM) result.getData();
            list.add(data);
        }
        return Result.ok(list);
    }

    @Override
    public Result getFens(String q, Integer p, User user) {
        if (q != null) {
            // 搜索功能
            List<FansConcern> concerns = fansConcernServer.getFensByUserId(user.getId());
            if (concerns.isEmpty()) {
                return Result.error("没有数据");
            }
            List<Integer> ids = concerns.stream().map(FansConcern::getConcern).collect(Collectors.toList());
            List<UserInfoVM> userInfoVMS = userInfoService.queryUserBaseInfoByPetNameAndUserIds(q, ids);
            return Result.ok(userInfoVMS);
        }
        List<FansConcern> concerns = fansConcernServer.getFensByUserId(user.getId(),p);
        if (concerns.isEmpty()) {
            return Result.error("没有数据");
        }
        ArrayList<UserInfoVM> list = new ArrayList<>();
        for (FansConcern fansConcern : concerns) {
            Result result = userInfoService.userInfo(fansConcern.getMe(),
                    Collections.singletonList(""), user);
            UserInfoVM data = (UserInfoVM) result.getData();
            list.add(data);
        }
        return Result.ok(list);
    }
}
