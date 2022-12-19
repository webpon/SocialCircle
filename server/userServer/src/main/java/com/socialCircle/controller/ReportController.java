package com.socialCircle.controller;

import com.socialCircle.entity.Report;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.ReportService;
import com.socialCircle.vm.ReportVM;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @PostMapping
    public Result rep(@RequestBody ReportVM report,
                      @RequestAttribute User user){
        return reportService.addRep(report, user);
    }
}
