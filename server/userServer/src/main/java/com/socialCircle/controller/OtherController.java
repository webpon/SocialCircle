package com.socialCircle.controller;

import cn.hutool.core.date.DateUtil;
import com.socialCircle.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtherController {

    @GetMapping("/time")
    public Result geTime(){
        long time = DateUtil.date().getTime();
        return Result.ok(time);
    }
}
