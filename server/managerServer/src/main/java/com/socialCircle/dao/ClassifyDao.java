package com.socialCircle.dao;

import com.socialCircle.entity.Classify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassifyDao {
    List<Classify> getClassify();

    Boolean addClassify(Classify classify);

    Boolean deleteById(@Param("ids") List<Integer> ids);
}
