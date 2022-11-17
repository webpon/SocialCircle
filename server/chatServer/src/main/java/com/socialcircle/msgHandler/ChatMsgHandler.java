package com.socialcircle.msgHandler;

import com.socialCircle.entity.ChatMsg;

@MsgHandlerType(value = "chat", clazz = ChatMsg.class)
public class ChatMsgHandler extends BaseMsgHandler {
}
