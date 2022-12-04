package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 是否关注了他
     *
     * @param userId        自己
     * @param concernUserId 被关注的人
     * @return 是则true
     */
    @Override
    public FansConcern meConcernHeByUserId(Integer userId, Integer concernUserId) {
        FansConcern fansConcern = fansConcernDao.selectOne(new QueryWrapper<FansConcern>()
                .eq("me", userId)
                .eq("concern", concernUserId));
        return fansConcern;
    }

    @Override
    public Boolean unConcern(Integer userId, Integer concernUserId) {
        int i = fansConcernDao.delete(new QueryWrapper<FansConcern>()
                .eq("me", userId)
                .eq("concern", concernUserId));
        return i > 0;
    }

    /**
     * 回去某个粉丝
     *
     * @param userId        自己
     * @param concernUserId 粉丝
     * @return 返回粉丝备注
     */
    @Override
    public FansConcern getOneFans(Integer userId, Integer concernUserId) {
        return fansConcernDao.selectOne(new QueryWrapper<FansConcern>()
                .eq("concern", userId)
                .eq("me", concernUserId));
    }

    /**
     * 关注
     *
     * @param fansConcern
     */
    @Override
    public Boolean Concern(FansConcern fansConcern) {
        int insert = fansConcernDao.insert(fansConcern);
        return insert == 1;
    }

    /**
     * 分页获取关注
     *
     * @param id 我
     * @param p  页码
     */
    @Override
    public List<FansConcern> getConcernByUserId(Integer id, Integer p) {
        return fansConcernDao.selectList(new QueryWrapper<FansConcern>()
                .eq("me", id).last("limit " + (p - 1) * 10 + "," + 10));
    }

    @Override
    public List<FansConcern> getFensByUserId(Integer id) {
        return fansConcernDao.selectList(new QueryWrapper<FansConcern>().eq("concern", id));
    }

    @Override
    public List<FansConcern> getFensByUserId(Integer id, Integer p) {
        return fansConcernDao.selectList(new QueryWrapper<FansConcern>()
                .eq("concern", id).last("limit " + (p - 1) * 10 + "," + 10));
    }
}