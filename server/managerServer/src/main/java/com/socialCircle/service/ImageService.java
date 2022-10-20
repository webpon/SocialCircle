package com.socialCircle.service;

import com.socialCircle.entity.Image;

import java.util.List;

public interface ImageService {
    /**
     * 安装举报id查询图片
     */
    List<Image> queryByReportId(Integer id);
}
