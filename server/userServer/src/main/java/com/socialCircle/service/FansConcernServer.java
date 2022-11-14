package com.socialCircle.service;

import com.socialCircle.entity.FansConcern;

import java.util.List;

public interface FansConcernServer {
    List<FansConcern> getConcernByUserId(Integer id);
}
