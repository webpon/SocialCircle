package com.socialCircle.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Message {
    public Message() {
    }

    public <T extends Message> Message(T  message) {
        this.setForm(message.getForm());
        message.setForm(null);
        this.setTo(message.getTo());
        message.setTo(null);
        this.setMsg(JSON.toJSONString(message));

    }

    private String type;
    private String msg;
    private Integer form;
    private Integer to;
    private String sendTime;
    @Id
    private String _id;
}
