package com.socialCircle.dao;

import com.socialCircle.entity.Image;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageDao {
    /**
     * 按照举报id查询图片
     */
    List<Image> queryByReportId(@Param("id") Integer id);

    List<Image> queryByDynamicId(@Param("id") Integer id);

    void deleteDynamicById(List<Integer> ids);

    Boolean save(List<Image> images);
}
