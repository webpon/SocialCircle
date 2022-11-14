package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.Friend;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendDao extends BaseMapper<Friend> {
}
