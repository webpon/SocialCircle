package com.socialCircle.service;

import java.util.List;

public interface UserHobbyService {
    /**
     * 按照兴趣id删除
     * @param ids
     */
    void deleteByHobbyId(List<Integer> ids);
}
