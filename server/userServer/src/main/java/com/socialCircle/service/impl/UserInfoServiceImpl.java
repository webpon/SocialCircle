package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.UserInfoDao;
import com.socialCircle.entity.*;
import com.socialCircle.service.*;
import com.socialCircle.vm.UserInfoVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private FansConcernServer fansConcernServer;
    @Resource
    private FriendServer friendServer;

    @Override
    public void save(SignIn user) {
        userInfoDao.save(user);
    }

    @Override
    public Result userInfo(Integer userId, List<String> fields, User user) {
        UserInfoVM info = userInfoDao.getInfo(userId);
        if (info == null) {
            return Result.error("没有当前用户");
        }
        if (fields != null) {
            fields.forEach(s -> {
                if ("work".equals(s)) {
                    info.setWorkExperiences(workExperienceService.getWorkExperienceByUserId(userId));
                } else if ("hobby".equals(s)) {
                    info.setHobbies(userHobbyService.getUserHobbiesByUserId(userId));
                }
            });
        }
        if (!userId.equals(user.getId())) {
            FansConcern fansConcern = fansConcernServer.meConcernHeByUserId(user.getId(), userId);
            info.setConcern(fansConcern != null);
            FansConcern fansConcern1 = fansConcernServer.meConcernHeByUserId(userId, user.getId());
            info.setFens(fansConcern1 != null);
            Friend friend = friendServer.heIsFriend(user.getId(), userId);
            info.setFriend(friend != null);
            // 备注
            if (info.getConcern()){
                info.setRemarks(fansConcern.getRemarks());
            } else if (info.getFens()){
                info.setRemarks(fansConcern1.getRemarks());
            } else if (info.getFriend()){
                info.setRemarks(friend.getRemarks());
            }
        }
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

    /**
     * 通过模糊查询昵称和id获取基本信息
     *
     * @param q   昵称
     * @param ids id
     * @return
     */
    @Override
    public List<UserInfoVM> queryUserBaseInfoByPetNameAndUserIds(String q, List<Integer> ids) {
        List<UserInfo> userInfos = userInfoDao.selectList(new QueryWrapper<UserInfo>()
                .like("pet_name", "%" + q + "%").in("user_id", ids));
        ArrayList<UserInfoVM> userInfoVMS = new ArrayList<>();
        for (UserInfo userInfo : userInfos) {
            UserInfoVM userInfoVM = new UserInfoVM();
            userInfoVM.setHeadIcon(userInfo.getHeadIcon());
            userInfoVM.setPetName(userInfo.getPetName());
            userInfoVM.setGender(userInfo.getGender());
            userInfoVMS.add(userInfoVM);
        }

        return userInfoVMS;
    }

}
