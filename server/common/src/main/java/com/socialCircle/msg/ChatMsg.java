package com.socialCircle.msg;

import com.socialCircle.msg.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatMsg extends Message {
    private String content;
    private Long mId;
}
