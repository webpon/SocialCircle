package com.socialCircle.controller.user;

import com.socialCircle.entity.Result;
import com.socialCircle.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
