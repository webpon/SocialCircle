package com.socialCircle.service.impl;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.ReportDao;
import com.socialCircle.entity.Image;
import com.socialCircle.entity.Report;
import com.socialCircle.entity.Result;
import com.socialCircle.service.ImageService;
import com.socialCircle.service.ReportService;
import com.socialCircle.vm.ReportVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportDao reportDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ImageService imageService;

    @Override
    public Result query(Integer p) {
        if (p == null) p = 1;
        // 获取缓存的里的对象
        List<ReportVM> bean = redisUtil.getBeans(RedisKey.REPORT_QUERY_KEY + p, ReportVM.class);
        if (bean == null) {
            // 没有就查询
            List<Report> reports = queryByPage(p);
            ArrayList<ReportVM> reportVMS = new ArrayList<>();
            reports.forEach(item -> {
                ReportVM vm = new ReportVM(item);
                List<Image> images = imageService.queryByReportId(item.getId());
                vm.setImages(images);
                reportVMS.add(vm);
            });
            // 保存缓存
            redisUtil.save(RedisKey.REPORT_QUERY_KEY + p, reportVMS);
            if (reportVMS.isEmpty()){
                return Result.error("没有数据",ResultCode.NOT_HAVE_DATA);
            }
            return Result.ok(reportVMS, ResultCode.HAVE_DATA);
        }
        return Result.ok(bean, ResultCode.HAVE_DATA);
    }

    private List<Report> queryByPage(Integer p) {
        p--;
        p *= 15;
        return reportDao.query(p);
    }
}
