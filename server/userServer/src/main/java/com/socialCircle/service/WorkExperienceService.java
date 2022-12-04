package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.entity.WorkExperience;

import java.util.List;

public interface WorkExperienceService {
    List<WorkExperience> getWorkExperienceByUserId(Integer id);

    Result addWorkExperience(User user, WorkExperience workExperience);

    Result deleteWorkExperience(Integer id, Integer userId);
}
