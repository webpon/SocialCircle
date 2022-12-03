package com.socialCircle.msg;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Message<T extends Message> {
    public Message() {
    }

    public Message(T  message) {
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
    private List<T> list;
    private Long mId;
}
