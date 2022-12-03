package com.socialCircle.service;

import com.socialCircle.entity.Blacklist;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;

public interface BlacklistService {
    Result addBlacklist(Blacklist blacklist, User user);

    Result getBlacklistByUserId(Integer id);
}
