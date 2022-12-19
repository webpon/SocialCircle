package com.socialCircle.controller;


import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.SealNumberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sealNumber")
public class SealNumberController {
    @Resource
    private SealNumberService sealNumberService;

    @GetMapping
    public Result querySealNumber(Integer id){
        return sealNumberService.querySealNumber(id);
    }
}
