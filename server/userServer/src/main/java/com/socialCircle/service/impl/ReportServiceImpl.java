package com.socialCircle.service.impl;

import com.socialCircle.dao.ReportDao;
import com.socialCircle.entity.Image;
import com.socialCircle.entity.Report;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.ImageService;
import com.socialCircle.service.ReportService;
import com.socialCircle.vm.ReportVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportDao dao;
    @Resource
    private ImageService imageService;

    @Override
    public Result addRep(ReportVM reportVM, User user) {
        Report report = new Report();
        report.setContent(reportVM.getContent());
        report.setReportUserId(reportVM.getReportUserId());
        report.setUserId(user.getId());
        int insert = dao.insert(report);
        if (insert != 1){
            return Result.error("举报失败");
        }
        List<Image> images = reportVM.getImages();
        if (images != null && !images.isEmpty()) {
            images.forEach(item -> item.setReportId(report.getId()));
            imageService.saveList(images);
        }
        return Result.ok("举报成功，等待审核");
    }
}
