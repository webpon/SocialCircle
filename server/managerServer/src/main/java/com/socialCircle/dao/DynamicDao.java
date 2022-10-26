package com.socialCircle.dao;

import com.socialCircle.entity.Dynamic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicDao {
    List<Dynamic> query(@Param("p") Integer p, @Param("classify") Integer classify);

    Boolean deleteDynamicById(@Param("ids") List<Integer> ids);

    Boolean save(Dynamic dynamic);

    void updateByTopicIds(@Param("ids") List<Integer> ids);
}
