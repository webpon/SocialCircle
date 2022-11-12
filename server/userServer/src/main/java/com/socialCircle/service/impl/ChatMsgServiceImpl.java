package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.ChatMsgDao;
import com.socialCircle.entity.ChatMsg;
import com.socialCircle.entity.User;
import com.socialCircle.service.ChatMsgService;
import com.socialCircle.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChatMsgServiceImpl implements ChatMsgService {
    @Resource
    private ChatMsgDao chatMsgDao;
    @Resource
    private UserInfoService userInfoService;

    @Override
    public void save(ChatMsg chatMsg) {
        User user = new User();
        user.setId(chatMsg.getTo());
        if (userInfoService.userInfo(user).getData() != null)
            chatMsgDao.insert(chatMsg);
    }

    @Override
    public List<ChatMsg> getMsgesByUserId(Integer id) {
        return chatMsgDao.selectList(new QueryWrapper<ChatMsg>().eq("`to`", id));
    }
}
