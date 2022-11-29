package com.socialCircle.service;

import com.socialCircle.entity.Friend;

import java.util.List;

public interface FriendServer {
    List<Friend> getByMeUserId(Integer id);
}
