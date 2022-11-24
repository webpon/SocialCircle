package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.UserInfoDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.service.UserHobbyService;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.WorkExperienceService;
import com.socialCircle.vm.UserInfoVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private WorkExperienceService workExperienceService;
    @Resource
    private UserHobbyService userHobbyService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void save(SignIn user) {
        userInfoDao.save(user);
    }

    @Override
    public Result userInfo(Integer userId, List<String> fields) {
        UserInfoVM info = userInfoDao.getInfo(userId);
        if (info == null) {
            return Result.error("没有当前用户");
        }
        if (fields == null) {
            info.setWorkExperiences(workExperienceService.getWorkExperienceByUserId(userId));
            info.setHobbies(userHobbyService.getUserHobbiesByUserId(userId));
            return Result.ok(info);
        }

        fields.forEach(s -> {
            if ("work".equals(s)) {
                info.setWorkExperiences(workExperienceService.getWorkExperienceByUserId(userId));
            } else if ("hobby".equals(s)) {
                info.setHobbies(userHobbyService.getUserHobbiesByUserId(userId));
            }
        });
        return Result.ok(info);
    }

    /**
     * 修改个人信息
     */
    @Override
    public Result updateUserInfo(SignIn signIn, User user) {
        signIn.setId(user.getId());
        if (userInfoDao.updateUserInfo(signIn)) {
            return Result.ok("修改成功", signIn);
        }
        return Result.error("修改失败");
    }

}
