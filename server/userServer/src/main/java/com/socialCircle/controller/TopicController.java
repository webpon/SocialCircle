package com.socialCircle.controller;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;
import com.socialCircle.entity.User;
import com.socialCircle.service.TopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Resource
    private TopicService topicService;


    @GetMapping
    public Result getTopic(@RequestParam(defaultValue = "1") Integer p){
        return topicService.getTopic(p);
    }

    @GetMapping("/my")
    public Result getTopic(@RequestParam(defaultValue = "1") Integer p,
                           @RequestAttribute User user){
        return topicService.getTopic(p, user);
    }

    @DeleteMapping
    public Result deleteTopic(@RequestParam Integer id,
                              @RequestAttribute User user){
        return topicService.deleteTopic(id,user);
    }

    @PostMapping
    public Result addTopic(@RequestBody Topic topic,
                            @RequestAttribute User user){
        return topicService.addTopic(topic, user);
    }

}
