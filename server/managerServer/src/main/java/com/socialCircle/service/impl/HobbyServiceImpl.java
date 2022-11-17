package com.socialCircle.service.impl;

import com.socialCircle.common.RedisUtil;
import com.socialCircle.constant.RedisKey;
import com.socialCircle.dao.HobbyDao;
import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.UserHobby;
import com.socialCircle.service.HobbyService;
import com.socialCircle.service.UserHobbyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.socialCircle.constant.RedisKey.HOBBY;

@Service
public class HobbyServiceImpl implements HobbyService {

    @Resource
    private HobbyDao hobbyDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserHobbyService userHobbyService;

    /**
     * 查询兴趣
     */
    @Override
    public Result getHobbies() {
        Map<String, List<Hobby>> beans = redisUtil.getMap(HOBBY, Hobby.class);
        if (beans == null) {
            List<Hobby> hobbies = hobbyDao.getHobbies();
            HashMap<String, List<Hobby>> map = new HashMap<>();
            hobbies.forEach(item -> {
                List<Hobby> hobbies1 = map.get(item.getTitle());
                if (hobbies1 == null) {
                    hobbies1 = new ArrayList<>();
                }
                hobbies1.add(item);
                map.put(item.getTitle(), hobbies1);
            });
            beans = map;
            redisUtil.save(HOBBY, beans);
        }
        return Result.ok(beans);
    }

    /**
     * 添加兴趣
     *
     * @param hobby 兴趣信息
     */
    @Override
    public Result addHobby(Hobby hobby) {
        if (hobbyDao.addHobby(hobby)){
            redisUtil.delete(HOBBY);
            return Result.ok(hobby);
        }
        return Result.error("添加失败");
    }

    /**
     * 删除兴趣
     *
     * @param ids 兴趣id
     */
    @Override
    public Result deleteHobby(List<Integer> ids) {
        userHobbyService.deleteByHobbyId(ids);
        if (hobbyDao.deleteHobby(ids)){
            redisUtil.delete(HOBBY);
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }
}
