package com.socialCircle.service.impl;

import com.socialCircle.dao.ImageDao;
import com.socialCircle.entity.Image;
import com.socialCircle.service.ImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    private ImageDao imageDao;
    @Override
    public List<Image> queryByReportId(Integer id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return imageDao.queryByReportId(id);
    }

    @Override
    public List<Image> queryByDynamicId(Integer id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return imageDao.queryByDynamicId(id);
    }

    @Override
    public void deleteDynamicById(List<Integer> ids) {
        imageDao.deleteDynamicById(ids);
    }

    @Override
    public Boolean save(List<Image> images) {
        return imageDao.save(images);
    }
}
