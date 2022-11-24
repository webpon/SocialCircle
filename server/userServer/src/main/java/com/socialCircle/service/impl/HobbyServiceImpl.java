package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.dao.HobbyDao;
import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.Result;
import com.socialCircle.service.HobbyService;
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

    @Override
    public Result getHobbies() {
        Map<String, List<Hobby>> beans = redisUtil.getMap(HOBBY, Hobby.class);
        if (beans == null) {
            List<Hobby> hobbies = hobbyDao.selectList(new QueryWrapper<Hobby>());
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

    @Override
    public List<Hobby> getHobbiesByIds(List<Integer> list) {
        return hobbyDao.selectBatchIds(list);
    }

}
