package com.socialCircle.service;

import com.socialCircle.entity.WorkExperience;

import java.util.List;

public interface WorkExperienceService {
    List<WorkExperience> getWorkExperienceByUserId(Integer id);
}
