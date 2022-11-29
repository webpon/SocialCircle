package com.socialcircle.msgHandler;

import com.alibaba.fastjson.JSON;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.entity.*;
import com.socialcircle.config.WsSessionManager;
import com.socialcircle.ws.HttpAuthHandler;
import lombok.Getter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.socialCircle.constant.RedisKey.LOGIN;

@Getter
public class BaseMsgHandler<T extends Message> {
    private String type;
    private Class<T> clazz;
    private Boolean save;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    protected MongoTemplate mongoTemplate;

    public BaseMsgHandler() {
        Class<? extends BaseMsgHandler> aClass = this.getClass();
        MsgHandlerType annotation = aClass.getAnnotation(MsgHandlerType.class);
        if (annotation != null) {
            type = annotation.value();
            clazz = (Class<T>) annotation.clazz();
            save = annotation.save();
        }
        HttpAuthHandler.msgHandlers.add(this);
    }

    public void receiveHandler(WebSocketSession session, User user) {
        if (!save){
            return;
        }
        try {
            Query query = new Query();
            Criteria criteria = new Criteria();
            criteria.andOperator(Criteria.where("to").is(user.getId().toString()));
            query.addCriteria(criteria);
            List<T> msgList = mongoTemplate.find(query, clazz, type);
            if (!msgList.isEmpty()) {
                T msg = msgList.get(0);
                List<T> list = msg.getList();
                list.forEach(dynamicMsg -> {
                    // 发送消息
                    Message message = new Message(dynamicMsg);
                    message.setTo(user.getId());
                    String s = JSON.toJSONString(message);
                    try {
                        session.sendMessage(new TextMessage(s));
                    } catch (IOException ignored) {}
                });
                // 删除动态记录
                mongoTemplate.remove(
                        new Query(Criteria.where("_id").is(msg.get_id())),
                        ChatMsg.class,type);
            }
        } catch (Exception ignored) {}

    }

    public void sendHandler(Message message) throws IOException{
        Integer bean = redisUtil.getBean(LOGIN + message.getTo(), Integer.class);

        // 判断是否在线
        if (bean != null) {
            String s = JSON.toJSONString(message);
            WebSocketSession wss = WsSessionManager.get(message.getTo().toString());
            wss.sendMessage(new TextMessage(s));
            return;
        }
        if (save) {
            saveHandler(message);
        }
    }

    public void saveHandler(Message message){
        T msg = JSON.parseObject(message.getMsg(), clazz);
        msg.setForm(message.getForm());
        msg.setTo(message.getTo());
        Query query = new Query();
        query.addCriteria(Criteria.where("to").is(msg.getTo().toString()));
        Update update = new Update();
        update.push("list", msg);
        mongoTemplate.upsert(query, update, type);
    }

}
