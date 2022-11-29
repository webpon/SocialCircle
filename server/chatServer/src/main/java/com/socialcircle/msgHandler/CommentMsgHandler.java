package com.socialcircle.msgHandler;

import com.socialCircle.entity.CommentMsg;

@MsgHandlerType(value = "comment", clazz = CommentMsg.class)
public class CommentMsgHandler extends BaseMsgHandler {
}
