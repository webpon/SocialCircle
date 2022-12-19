package com.socialCircle.service;

import com.socialCircle.entity.Image;

import java.util.List;

public interface ImageService {
    List<Image> queryByDynamicId(Integer id);

    void saveListByDynamicId(List<Image> images, Integer id);

    void deleteByDynamicId(Integer id);

    void saveList(List<Image> images);
}
