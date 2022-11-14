package com.socialCircle.controller;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.DynamicService;
import com.socialCircle.vm.DynamicVM;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dynamic")
public class DynamicController {

    @Resource
    private DynamicService dynamicService;

    @GetMapping
    public Result getDynamics(@RequestParam(defaultValue = "1") Integer p,
                              Integer classify) {
        return dynamicService.getDynamics(p, classify);
    }

    @GetMapping("/byUserId")
    public Result getDynamics(@RequestParam(defaultValue = "1") Integer p,
                              Integer userId,
                              @RequestAttribute User user) {
        return dynamicService.getDynamicsByUserId(p, userId != null ? userId : user.getId());
    }

    @GetMapping("/byTopicId")
    public Result getDynamicsByTopicId(@RequestParam(defaultValue = "1") Integer p,
                                       Integer topicId) {
        return dynamicService.getDynamicsByTopicId(p, topicId);
    }

    @GetMapping("/concern")
    public Result getDynamics(@RequestParam(defaultValue = "1") Integer p,
                              @RequestAttribute User user) {
        return dynamicService.getDynamicsByConcern(p, user.getId());
    }

    @PostMapping
    public Result addDynamic(@RequestBody DynamicVM dynamicVM,
                             @RequestAttribute User user) {
        return dynamicService.addDynamic(dynamicVM, user);
    }

    @DeleteMapping
    public Result deleteDynamic(@RequestParam Integer id,
                                @RequestAttribute User user) {
        return dynamicService.deleteDynamicById(id, user);
    }
}
