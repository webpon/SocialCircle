package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.service.ReportService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Resource
    private ReportService reportService;

    @GetMapping("{p}")
    public Result query(@PathVariable("p") Integer p){
        return reportService.query(p);
    }

    @DeleteMapping
    public Result deleteById(Integer id){
        return reportService.deleteById(id);
    }
}
