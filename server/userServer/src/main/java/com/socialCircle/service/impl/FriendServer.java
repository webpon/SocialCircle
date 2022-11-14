package com.socialCircle.service.impl;

import com.socialCircle.entity.Friend;

import java.util.List;

public interface FriendServer {
    List<Friend> getByMeUserId(Integer id);
}
