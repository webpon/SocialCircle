package com.socialcircle.msgHandler;

import com.socialCircle.entity.User;
import com.socialCircle.msg.BlackMsg;
import com.socialCircle.msg.ChatMsg;
import com.socialCircle.msg.Message;
import com.socialcircle.server.BlacklistServer;

import javax.annotation.Resource;
import java.io.IOException;

@MsgHandlerType(value = "chat", clazz = ChatMsg.class)
public class ChatMsgHandler extends BaseMsgHandler<ChatMsg> {
    @Resource
    private BlacklistServer blacklistServer;
    @Override
    public void sendHandler(Message message, User user) throws IOException {
        Integer me = user.getId();
        Integer to = message.getTo();
        Boolean heBlack = blacklistServer.ifHeBlack(me, to);
        Boolean meBlack = blacklistServer.ifMeBlack(me, to);
        if (heBlack && meBlack) {
            // 是拉黑
            // 设置信息
            BlackMsg blackMsg = new BlackMsg();
            if (heBlack){
                blackMsg.setType("1");
            }
            if (meBlack){
                blackMsg.setType("2");
            }
            blackMsg.setUserId(to);
            message = new Message(blackMsg);
            message.setForm(to);
            message.setTo(me);
        }
        // 发送消息
        super.sendHandler(message, user);
    }
}
