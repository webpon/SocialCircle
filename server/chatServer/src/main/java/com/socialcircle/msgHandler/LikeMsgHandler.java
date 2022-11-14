package com.socialcircle.msgHandler;

import com.alibaba.fastjson.JSON;
import com.socialCircle.entity.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Component
public class LikeMsgHandler extends AbstractMsgHandler{
    public LikeMsgHandler() {
        super();
        setType("like");
        setClazz(LikeMsg.class);
    }

    @Override
    public void saveHandler(Message message) {
        LikeMsg msg = JSON.parseObject(message.getMsg(), LikeMsg.class);
        msg.setForm(message.getForm());
        msg.setTo(message.getTo());
        Query query = new Query();
        query.addCriteria(Criteria.where("to").is(msg.getTo().toString()));
        Update update = new Update();
        update.push("list", msg);
        mongoTemplate.upsert(query, update, type);

    }
}
