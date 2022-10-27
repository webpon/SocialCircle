package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.vm.DynamicVM;

import java.util.List;

public interface DynamicService {
    /**
     * 获取动态
     * @param p 第几页
     * @param classify 分类id
     */
    Result getDynamic(Integer p, Integer classify);

    /**
     * 删除动态
     * @param ids 动态id集合
     */
    Result deleteDynamicById(List<Integer> ids);

    /**
     * 添加动态
     * @param dynamicVM 动态内容
     */
    Result addDynamicById(DynamicVM dynamicVM);

    /**
     * 修改动态话题
     * @param ids
     */
    void updateByTopicIds(List<Integer> ids);
}
