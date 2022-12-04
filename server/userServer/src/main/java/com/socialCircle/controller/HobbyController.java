package com.socialCircle.controller;

import com.socialCircle.entity.Result;
import com.socialCircle.service.HobbyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/hobby")
public class HobbyController {

    @Resource
    private HobbyService hobbyService;

    @GetMapping
    public Result getHobbies(){
        return hobbyService.getHobbies();
    }
}
