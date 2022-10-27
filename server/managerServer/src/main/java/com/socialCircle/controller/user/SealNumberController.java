package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.SealNumber;
import com.socialCircle.service.SealNumberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sealNumber")
public class SealNumberController {
    @Resource
    private SealNumberService sealNumberService;

    @PostMapping
    public Result sealNumber(@RequestBody SealNumber sealNumber){
        return sealNumberService.sealNumber(sealNumber);
    }

}
