package com.socialCircle.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.socialCircle.entity.Dynamic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DynamicDao extends BaseMapper<Dynamic> {
    List<Dynamic> getTop();
}
