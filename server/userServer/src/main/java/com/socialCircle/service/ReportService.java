package com.socialCircle.service;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.vm.ReportVM;

public interface ReportService {

    Result addRep(ReportVM report, User user);
}
