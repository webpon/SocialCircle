package com.socialCircle.service;

import com.socialCircle.entity.ChatMsg;

import java.util.List;

public interface ChatMsgService {
    void save(ChatMsg chatMsg);

    List<ChatMsg> getMsgesByUserId(Integer id);
}
