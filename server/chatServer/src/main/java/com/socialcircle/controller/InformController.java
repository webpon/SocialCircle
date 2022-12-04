package com.socialcircle.controller;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.msg.Message;
import com.socialcircle.msgHandler.BaseMsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/inform")
public class InformController {
    @Autowired
    public List<BaseMsgHandler> msgHandlers = new ArrayList<>();

    @Resource
    private RedisUtil redisUtil;

    @GetMapping
    public void informUser(String key)  {
        Message message = redisUtil.getBean(key, Message.class);
        if (message == null) {
            return;
        }
        msgHandlers.forEach(handler -> {
            if (handler.getType().equals(message.getType())) {
                try {
                    handler.sendHandler(message, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
