package com.socialcircle.msgHandler;

import com.socialCircle.entity.DynamicMsg;
import org.springframework.stereotype.Component;

@MsgHandlerType(value = "dynamic", clazz = DynamicMsg.class)
public class DynamicMsgHandler extends BaseMsgHandler {
}
