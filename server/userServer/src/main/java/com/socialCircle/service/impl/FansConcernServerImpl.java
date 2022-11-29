package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.FansConcernDao;
import com.socialCircle.entity.FansConcern;
import com.socialCircle.service.FansConcernServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FansConcernServerImpl implements FansConcernServer {

    @Resource
    private FansConcernDao fansConcernDao;
    @Override
    public List<FansConcern> getConcernByUserId(Integer id) {
        return fansConcernDao.selectList(new QueryWrapper<FansConcern>().eq("me", id));
    }
}
