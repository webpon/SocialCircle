package com.socialCircle.dao;

import com.socialCircle.entity.Image;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageDao {
    /**
     * 安装举报id查询图片
     */
    List<Image> queryByReportId(@Param("id") Integer id);
}
