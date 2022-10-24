package com.socialCircle.controller;

import com.socialCircle.entity.Dynamic;
import com.socialCircle.entity.Result;
import com.socialCircle.service.DynamicService;
import com.socialCircle.vm.DynamicVM;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dynamic")
public class ManageDynamicController {

    @Resource
    private DynamicService dynamicService;

    @GetMapping
    public Result getDynamic(@RequestParam Integer p){
        return dynamicService.getDynamic(p);
    }

    @DeleteMapping
    public Result deleteDynamic(@RequestParam List<Integer> ids){
        return dynamicService.deleteDynamicById(ids);
    }

    @PostMapping
    public Result addDynamic(@RequestBody DynamicVM dynamicVM){
        return dynamicService.addDynamicById(dynamicVM);
    }
}
