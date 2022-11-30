package com.socialCircle.controller;


import com.socialCircle.entity.Result;
import com.socialCircle.entity.TopicConcern;
import com.socialCircle.entity.User;
import com.socialCircle.service.TopicConcernServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/topic/concern")
public class TopicConcernController {

    @Resource
    private TopicConcernServer topicConcernServer;

    @PostMapping
    public Result concernTopic(@RequestBody TopicConcern topicConcern,
                               @RequestAttribute User user){
        return topicConcernServer.concernTopic(topicConcern,user);
    }

    @GetMapping
    public Result getConcernTopic(@RequestParam(defaultValue = "1") Integer p,
                                  @RequestAttribute User user){
        return topicConcernServer.getConcernTopic(p,user);
    }

}
