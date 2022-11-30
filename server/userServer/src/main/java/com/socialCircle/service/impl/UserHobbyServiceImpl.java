package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.constant.RedisQuery;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.socialCircle.constant.RedisKey.USER_HOBBY;

@Service
public class UserHobbyServiceImpl implements UserHobbyService {
    @Resource
    private UserHobbyDao userHobbyDao;
    @Resource
    private HobbyService hobbyService;
    @Resource
    private RedisUtil redisUtil;

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
        redisUtil.delete(USER_HOBBY + user.getId());
        return Result.ok("添加成功");

    }

    @Override
    public Result deleteUserHobby(Integer hobbyId, User user) {
        int i = userHobbyDao.delete(new QueryWrapper<UserHobby>()
                .eq("user_id", user.getId())
                .eq("hobby_id", hobbyId));
        redisUtil.delete(USER_HOBBY + user.getId());
        return i > 0 ?
                Result.ok("删除成功") :
                Result.error("删除失败");
    }


    @Override
    public List<UserHobby> getUserHobbiesByUserId(Integer id) {
        RedisQuery<List<UserHobby>> redisQuery =
                new RedisQuery<>(USER_HOBBY, id.toString(), null, DateField.MINUTE, 30);
        RedisCommand<List<UserHobby>> command = (key) -> {
            return userHobbyDao.selectList(new QueryWrapper<UserHobby>().eq("user_id", id));
        };
        List<UserHobby> userHobbies = redisUtil.getBeans(redisQuery, command, UserHobby.class);
        if (userHobbies == null || userHobbies.size() == 0) {
            return null;
        }
        return userHobbies;
    }
}
