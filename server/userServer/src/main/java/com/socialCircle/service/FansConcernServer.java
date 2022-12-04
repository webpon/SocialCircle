package com.socialCircle.service;

import com.socialCircle.entity.FansConcern;

import java.util.List;

public interface FansConcernServer {

    /**
     * 获取我的关注
     */
    List<FansConcern> getConcernByUserId(Integer id);

    /**
     * 是否关注了他
     * @param userId 自己
     * @param concernUserId 被关注的人
     * @return 是则true
     */
    FansConcern meConcernHeByUserId(Integer userId, Integer concernUserId);

    Boolean unConcern(Integer userId, Integer concernUserId);

    /**
     * 回去某个粉丝
     * @param userId 自己
     * @param concernUserId 粉丝
     * @return 返回粉丝备注
     */
    FansConcern getOneFans(Integer userId, Integer concernUserId);

    /**
     * 关注
     */
    Boolean Concern(FansConcern fansConcern);

    /**
     * 分页获取关注
     * @param id 我
     * @param p 页码
     */
    List<FansConcern> getConcernByUserId(Integer id, Integer p);

    /**
     * 查询获取粉丝
     * @param id 我
     */
    List<FansConcern> getFensByUserId(Integer id);

    /**
     * 分页获取粉丝
     * @param id 我
     * @param p 页码
     */
    List<FansConcern> getFensByUserId(Integer id, Integer p);

}
