package com.socialCircle.service.impl;

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
    public List<Image> queryByReportId(Integer id) {
        if (id == null) {
            return null;
        }
        return imageDao.queryByReportId(id);
    }
}
