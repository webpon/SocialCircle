package com.socialCircle.dao;

import com.socialCircle.entity.WorkExperience;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkExperienceDao {
    List<WorkExperience> getWorkExperienceByUserId(@Param("id") Integer id);
}
