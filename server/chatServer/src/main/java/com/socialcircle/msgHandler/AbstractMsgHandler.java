package com.socialcircle.msgHandler;

import com.socialCircle.entity.User;
import com.socialCircle.entity.Message;
import com.socialcircle.ws.HttpAuthHandler;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public abstract class AbstractMsgHandler {
    protected String type;

    public AbstractMsgHandler() {
        HttpAuthHandler.msgHandlers.add(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract void receiveHandler(WebSocketSession session, User user);

    public abstract void sendHandler(Message message, WebSocketSession session) throws IOException;
}
