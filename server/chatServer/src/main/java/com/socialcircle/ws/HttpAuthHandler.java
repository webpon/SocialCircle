package com.socialcircle.ws;
import com.alibaba.fastjson.JSON;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.entity.User;
import com.socialcircle.config.WsSessionManager;
import com.socialcircle.msgHandler.BaseMsgHandler;
import com.socialCircle.msg.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class HttpAuthHandler extends TextWebSocketHandler {

    @Autowired
    public  List<BaseMsgHandler> msgHandlers = new ArrayList<>();

    @Resource
    private RedisUtil redisUtil;

    /**
     * socket 建立成功事件
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get("user");
        if (user != null) {
            // 用户连接成功，放入在线用户缓存
            WsSessionManager.add(user.getId().toString(), session);
            redisUtil.setIfAbsent(RedisKey.LOGIN+user.getId());
            for (BaseMsgHandler handler : msgHandlers) {
                handler.receiveHandler(session, user);
            }
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 接收消息事件
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User user = (User) session.getAttributes().get("user");
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Message message1 = JSON.parseObject(payload, Message.class);
        message1.setForm(user.getId());
        for (BaseMsgHandler handler : msgHandlers) {
            if (handler.getType().equals(message1.getType())) {
                handler.sendHandler(message1, user);
            }
        }
    }

    /**
     * socket 断开连接时
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");
        if (user != null) {
            redisUtil.delete(RedisKey.LOGIN+user.getId().toString());
            // 用户退出，移除缓存
            WsSessionManager.remove(user.getId().toString());
        }
    }


}