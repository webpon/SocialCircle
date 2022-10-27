package com.socialCircle.controller;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.Topic;
import com.socialCircle.service.TopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/topic")
public class ManageTopicController {

    @Resource
    private TopicService topicService;

    @GetMapping
    public Result getTopic(@RequestParam(defaultValue = "1") Integer p){
        return topicService.getTopic(p);
    }

    @DeleteMapping
    public Result deleteTopic(@RequestParam List<Integer> ids){
        return topicService.deleteTopic(ids);
    }

    @PostMapping
    public Result addTopic(@RequestBody Topic topic){
        return topicService.addTopic(topic);
    }

}
