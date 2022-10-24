package com.socialCircle.service;

import com.socialCircle.entity.Image;

import java.util.List;

public interface ImageService {
    /**
     * 按照举报id查询图片
     */
    List<Image> queryByReportId(Integer id);

    /**
     * 按照动态id查询图片
     */
    List<Image> queryByDynamicId(Integer id);

    /**
     * 根据动态id删除
     * @param ids 动态id
     */
    void deleteDynamicById(List<Integer> ids);

    /**
     * 批量保存
     * @param images 图片
     */
    Boolean save(List<Image> images);

}
