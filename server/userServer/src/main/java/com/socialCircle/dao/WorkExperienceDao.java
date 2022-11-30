package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.WorkExperience;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkExperienceDao extends BaseMapper<WorkExperience> {
    List<WorkExperience> getWorkExperienceByUserId(@Param("id") Integer id);
}
