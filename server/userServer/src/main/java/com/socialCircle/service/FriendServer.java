package com.socialCircle.service;

import com.socialCircle.entity.FansConcern;
import com.socialCircle.entity.Friend;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;

import java.util.List;

public interface FriendServer {
    List<Friend> getByMeUserId(Integer id);

    /**
     * 关注
     */
    Result concern(FansConcern fansConcern, User user);

    /**
     * 他是不是你的好友
     * @param heId 他
     * @param userId 自己
     * @return 是true，反之
     */
    Friend heIsFriend(Integer heId, Integer userId);

    /**
     * 获取好友
     * @param q 搜索昵称
     * @param p 页
     * @param user 自己
     */
    Result getFriend(String q, Integer p, User user);

    /**
     * 获取关注列表
     * @param q 搜索昵称
     * @param p 页
     * @param user 自己
     */
    Result getConcern(String q, Integer p, User user);

    /**
     * 获取发送
     * @param q 搜索昵称
     * @param p 页
     * @param user 自己
     */
    Result getFens(String q, Integer p, User user);
}
