package com.socialCircle.controller;

import com.socialCircle.entity.Classify;
import com.socialCircle.entity.Result;
import com.socialCircle.service.ClassifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/classify")
public class ClassifyController {
    @Resource
    private ClassifyService classifyService;

    @GetMapping
    public Result getClassifies(){
        return classifyService.getClassifies();
    }
}
