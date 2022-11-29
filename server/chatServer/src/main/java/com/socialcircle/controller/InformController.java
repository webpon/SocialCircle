package com.socialcircle.controller;

import com.alibaba.fastjson.JSON;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.entity.Message;
import com.socialCircle.entity.User;
import com.socialcircle.config.WsSessionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

import java.io.IOException;

import static com.socialCircle.constant.RedisKey.LOGIN;
import static com.socialcircle.ws.HttpAuthHandler.msgHandlers;

@RestController
@RequestMapping("/inform")
public class InformController {

    @Resource
    private RedisUtil redisUtil;
    @GetMapping
    public void informUser(String key, @RequestAttribute User user)  {
        WebSocketSession session = WsSessionManager.get(user.getId().toString());
        if (session == null) {
            return;
        }
        Message message = redisUtil.getBean(key, Message.class);
        if (message == null) {
            return;
        }
        msgHandlers.forEach(handler -> {
            if (handler.getType().equals(message.getType())) {
                try {
                    handler.sendHandler(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
