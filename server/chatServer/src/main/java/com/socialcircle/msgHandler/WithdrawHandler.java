package com.socialcircle.msgHandler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.socialCircle.entity.User;
import com.socialCircle.msg.Message;
import com.socialCircle.msg.WithdrawMsg;
import com.socialcircle.config.WsSessionManager;
import lombok.extern.log4j.Log4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


import static com.socialCircle.constant.RedisKey.LOGIN;

@Log4j
@MsgHandlerType(value = "withdraw",
        clazz = WithdrawMsg.class,
        save = false,
        sort = 2)
public class WithdrawHandler extends BaseMsgHandler {

    @Override
    public void sendHandler(Message message, User user) throws IOException {
        Integer bean = redisUtil.getBean(LOGIN + message.getTo(), Integer.class);
        WithdrawMsg msg = JSON.parseObject(message.getMsg(), WithdrawMsg.class);

        String msgId = msg.getMsgId();
        // 字符串转时间戳
        long time = 0;
        try {
            time = Long.parseLong(msgId);
        } catch (NumberFormatException e) {
            log.error(e);
        }
        DateTime date = DateUtil.date(time);
        DateTime now = DateUtil.date();
        date = DateUtil.offsetMinute(date, 2);
        boolean b = date.getTime() > now.getTime();
        msg.setSuccess(b);
        if (b) {
            // 判断是否在线
            if (bean != null) {
                String s = JSON.toJSONString(message);
                WebSocketSession wss = WsSessionManager.get(message.getTo().toString());
                if (wss == null) {
                    redisUtil.delete(LOGIN + message.getTo());
                    saveHandler(message);
                }
                wss.sendMessage(new TextMessage(s));
                return;
            }
            super.saveHandler(message);
        }
        // 发送给撤回着
        String s = JSON.toJSONString(message);
        WebSocketSession wss = WsSessionManager.get(message.getForm().toString());
        wss.sendMessage(new TextMessage(s));
    }
}
