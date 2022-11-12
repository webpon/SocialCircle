package com.socialCircle.service.impl;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.dao.UserInfoDao;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.SignIn;
import com.socialCircle.entity.User;
import com.socialCircle.entity.WorkExperience;
import com.socialCircle.service.UserInfoService;
import com.socialCircle.service.WorkExperienceService;
import com.socialCircle.vm.UserInfoVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private WorkExperienceService workExperienceService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void save(SignIn user) {
        userInfoDao.save(user);
    }

    @Override
    public Result userInfo(User user) {
        UserInfoVM info = userInfoDao.getInfo(user.getId());
        if (info == null) {
            return Result.error("没有当前用户");
        }
        info.setWorkExperiences(workExperienceService.getWorkExperienceByUserId(user.getId()));
        return Result.ok(info);
    }

    /**
     * 修改个人信息
     *
     */
    @Override
    public Result updateUserInfo(SignIn signIn, User user) {
        signIn.setId(user.getId());
        if (userInfoDao.updateUserInfo(signIn)) {
            return Result.ok("修改成功",signIn);
        }
        return Result.error("修改失败");
    }

}
