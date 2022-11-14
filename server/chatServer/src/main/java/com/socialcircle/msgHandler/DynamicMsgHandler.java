package com.socialcircle.msgHandler;

import com.alibaba.fastjson.JSON;
import com.socialCircle.entity.ChatMsg;
import com.socialCircle.entity.DynamicMsg;
import com.socialCircle.entity.Message;
import com.socialCircle.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Component
public class DynamicMsgHandler extends AbstractMsgHandler{

    public DynamicMsgHandler() {
        super();
        setType("dynamic");
        setClazz(DynamicMsg.class);
    }

}
