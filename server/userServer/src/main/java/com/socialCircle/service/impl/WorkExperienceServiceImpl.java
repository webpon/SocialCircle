package com.socialCircle.service.impl;

import com.socialCircle.dao.WorkExperienceDao;
import com.socialCircle.entity.WorkExperience;
import com.socialCircle.service.WorkExperienceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorkExperienceServiceImpl implements WorkExperienceService {
    @Resource
    private WorkExperienceDao dao;
    @Override
    public List<WorkExperience> getWorkExperienceByUserId(Integer id) {
        return dao.getWorkExperienceByUserId(id);
    }
}
