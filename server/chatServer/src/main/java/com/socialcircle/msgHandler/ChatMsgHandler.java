package com.socialcircle.msgHandler;

import com.socialCircle.msg.ChatMsg;

@MsgHandlerType(value = "chat", clazz = ChatMsg.class)
public class ChatMsgHandler extends BaseMsgHandler {
}
