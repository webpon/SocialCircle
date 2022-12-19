package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.ImageDao;
import com.socialCircle.entity.Image;
import com.socialCircle.service.ImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageDao imageDao;
    @Override
    public List<Image> queryByDynamicId(Integer id) {
        return imageDao.selectList(new QueryWrapper<Image>().eq("dynamic_id", id));
    }

    @Override
    public void saveListByDynamicId(List<Image> images, Integer id) {
        images.forEach(image -> {
            image.setDynamicId(id);
            imageDao.insert(image);
        });
    }

    @Override
    public void deleteByDynamicId(Integer id) {
        imageDao.delete(new QueryWrapper<Image>().eq("dynamic_id", id));
    }

    @Override
    public void saveList(List<Image> images) {
        images.forEach(i -> imageDao.insert(i));
    }
}
