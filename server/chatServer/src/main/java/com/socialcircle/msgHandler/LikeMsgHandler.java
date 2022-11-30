package com.socialcircle.msgHandler;

import com.socialCircle.msg.LikeMsg;

@MsgHandlerType(value = "like", clazz = LikeMsg.class)
public class LikeMsgHandler extends BaseMsgHandler {
}
