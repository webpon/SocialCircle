package com.socialCircle.service;

import com.socialCircle.entity.Result;

public interface ReportService {
    Result query(Integer p);

    Result deleteById(Integer id);
}
