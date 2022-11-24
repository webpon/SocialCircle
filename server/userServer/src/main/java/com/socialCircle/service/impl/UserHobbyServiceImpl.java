package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.UserHobbyDao;
import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.entity.UserHobby;
import com.socialCircle.service.HobbyService;
import com.socialCircle.service.UserHobbyService;
import com.socialCircle.vm.AddHobby;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserHobbyServiceImpl implements UserHobbyService {
    @Resource
    private UserHobbyDao userHobbyDao;
    @Resource
    private HobbyService hobbyService;

    @Override
    public Result addUserHobby(AddHobby addHobby, User user) {
        List<Integer> ids = addHobby.getIds();
        ids.forEach(id -> {
            UserHobby hobby = userHobbyDao.selectOne(new QueryWrapper<UserHobby>()
                    .eq("user_id", user.getId())
                    .eq("hobby_id", id));
            if (hobby != null) {
                return;
            }

            List<Integer> list = Collections.singletonList(id);
            List<Hobby> hobbies1 = hobbyService.getHobbiesByIds(list);
            if (hobbies1 == null || hobbies1.size() == 0) {
                return;
            }

            UserHobby userHobby = new UserHobby();
            userHobby.setHobbyId(id);
            userHobby.setHobbyName(hobbies1.get(0).getHobbyName());
            userHobby.setUserId(user.getId());

            userHobbyDao.insert(userHobby);
        });

        return Result.ok("添加成功");

    }

    @Override
    public List<UserHobby> getUserHobbiesByUserId(Integer id) {
        List<UserHobby> userHobbies =
                userHobbyDao.selectList(new QueryWrapper<UserHobby>().eq("user_id", id));
        if (userHobbies == null || userHobbies.size() == 0) {
            return null;
        }
        return userHobbies;
    }
}
