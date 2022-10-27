package com.socialCircle.service;

import com.socialCircle.entity.Classify;
import com.socialCircle.entity.Result;

import java.util.List;

public interface ClassifyService {
    /**
     * 获取分类
     */
    Result getClassify();

    /**
     * 添加分类
     */
    Result addClassify(Classify classify);

    /**
     * 删除分类
     */
    Result deleteClassify(List<Integer> ids);
}
