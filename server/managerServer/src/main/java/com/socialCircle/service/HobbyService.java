package com.socialCircle.service;

import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.Result;

import java.util.List;

public interface HobbyService {

    /**
     * 查询兴趣
     */
    Result getHobbies();

    /**
     * 添加兴趣
     * @param hobby 兴趣信息
     */
    Result addHobby(Hobby hobby);

    /**
     * 删除兴趣
     * @param ids 兴趣id
     */
    Result deleteHobby(List<Integer> ids);
}
