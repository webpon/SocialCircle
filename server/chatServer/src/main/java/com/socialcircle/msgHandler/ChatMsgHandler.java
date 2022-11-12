package com.socialcircle.msgHandler;

import com.alibaba.fastjson.JSON;
import com.socialCircle.entity.ChatMsg;
import com.socialCircle.entity.User;
import com.socialCircle.entity.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Configuration
public class ChatMsgHandler extends AbstractMsgHandler {

    @Resource
    private MongoTemplate mongoTemplate;

    public ChatMsgHandler() {
        super();
        this.setType("chat");
    }



    @Override
    public void sendHandler(Message message, WebSocketSession session){
        ChatMsg chatMsg = JSON.parseObject(message.getMsg(), ChatMsg.class);
        chatMsg.setForm(message.getForm());
        chatMsg.setTo(message.getTo());
        Query query = new Query();
        query.addCriteria(Criteria.where("to").is(chatMsg.getTo().toString()));
        Update update = new Update();
        update.push("list", chatMsg);
        mongoTemplate.upsert(query, update, "chat");

    }

    @Override
    public void receiveHandler(WebSocketSession session, User user){
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("to").is(user.getId().toString()));
        query.addCriteria(criteria);
        List<ChatMsg> msgList = mongoTemplate.find(query, ChatMsg.class, "chat");
        if (!msgList.isEmpty()) {
            ChatMsg msg = msgList.get(0);
            msg.getList().forEach(chatMsg -> {
                // 发送消息
                Message message = new Message(chatMsg);
                message.setTo(user.getId());
                String s = JSON.toJSONString(message);
                try {
                    session.sendMessage(new TextMessage(s));
                } catch (IOException e) {
                }
            });
            // 删除聊天记录
            mongoTemplate.remove(
                    new Query(Criteria.where("_id").is(msg.get_id())),
                    ChatMsg.class,"chat");
        }
    }
}
