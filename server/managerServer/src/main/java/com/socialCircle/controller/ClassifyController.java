package com.socialCircle.controller;

import com.socialCircle.entity.Classify;
import com.socialCircle.entity.Result;
import com.socialCircle.service.ClassifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/classify")
public class ClassifyController {

    @Resource
    private ClassifyService classifyService;

    @GetMapping
    public Result getClassify(){
        return classifyService.getClassify();
    }

    @PostMapping
    public Result addClassify(@RequestBody Classify classify){
        return classifyService.addClassify(classify);
    }

    @DeleteMapping
    public Result deleteClassify(@RequestParam List<Integer> ids){
        return classifyService.deleteClassify(ids);
    }
}
