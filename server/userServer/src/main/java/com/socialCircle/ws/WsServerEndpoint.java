package com.socialCircle.ws;


import com.socialCircle.config.SessionManager;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/myWs/{token}")
@Component
public class WsServerEndpoint {

    private String token;
    /**
     * 连接成功
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String  token) {
        SessionManager.add(token,session);
        this.token = token;
        System.out.println("连接成功"+token);
    }

    /**
     * 连接关闭
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        SessionManager.removeAndClose(token);
        System.out.println("连接关闭");
    }

    /**
     * 接收到消息
     *
     * @param text
     */
    @OnMessage
    public void onMsg(String text) throws IOException {
        String[] split = text.split(":");
        SessionManager.get(split[0]).getAsyncRemote().sendText(split[1]);
    }
}