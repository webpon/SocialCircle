package com.socialCircle.dao;

import com.socialCircle.entity.Classify;
import com.socialCircle.vm.ClassifyVM;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassifyDao {
    List<ClassifyVM> getClassify();

    Boolean addClassify(Classify classify);

    Boolean deleteById(@Param("ids") List<Integer> ids);
}
