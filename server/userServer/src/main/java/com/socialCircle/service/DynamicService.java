package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.vm.DynamicVM;

public interface DynamicService {
    Result getDynamics(Integer p, Integer classify);

    Result getDynamicsByUserId(Integer p, Integer userId);

    Result addDynamic(DynamicVM dynamicVM, User user);

    Result deleteDynamicById(Integer id, User user);

    Result getDynamicsByConcern(Integer p, Integer id);

    Result getDynamicsByTopicId(Integer p, Integer topicId);

    DynamicVM getDynamicById(Integer dynamicId);
}
