package com.socialCircle.dao;

import com.socialCircle.entity.Report;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportDao  {
    List<Report> query(@Param("p") Integer p);

    Long count();

    boolean delete(Integer id);
}
