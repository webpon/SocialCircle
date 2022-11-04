package com.socialCircle.service.impl;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.ResultCode;
import com.socialCircle.dao.ReportDao;
import com.socialCircle.entity.Image;
import com.socialCircle.entity.Report;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.ImageService;
import com.socialCircle.service.ReportService;
import com.socialCircle.service.UserService;
import com.socialCircle.vm.ReportVM;
import com.socialCircle.vm.UserInfoVM;
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
    @Resource
    private UserService userService;

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
                User user = new User();
                user.setId(vm.getUserId());
                UserInfoVM userInfoVM = (UserInfoVM) userService.userInfo(user).getData();
                vm.setUser(userInfoVM);
                user.setId(vm.getReportUserId());
                UserInfoVM userInfoVM1 = (UserInfoVM) userService.userInfo(user).getData();
                vm.setReportUser(userInfoVM1);
                reportVMS.add(vm);
            });
            // 保存缓存
            redisUtil.save(RedisKey.REPORT_QUERY_KEY + p, reportVMS);
            if (reportVMS.isEmpty()){
                return Result.error("没有数据");
            }
            return Result.ok(reportVMS);
        }
        return Result.ok(bean);
    }

    private List<Report> queryByPage(Integer p) {
        p--;
        p *= 15;
        return reportDao.query(p);
    }
}
