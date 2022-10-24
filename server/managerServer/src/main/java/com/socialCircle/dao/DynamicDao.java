package com.socialCircle.dao;

import com.socialCircle.entity.Dynamic;
import com.socialCircle.vm.DynamicVM;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DynamicDao {
    List<Dynamic> query(@Param("p") Integer p);

    Boolean deleteDynamicById(@Param("ids") List<Integer> ids);

    Boolean save(Dynamic dynamic);
}
