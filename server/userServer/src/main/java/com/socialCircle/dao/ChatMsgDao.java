package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.ChatMsg;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMsgDao extends BaseMapper<ChatMsg> {
}
