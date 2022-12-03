package com.socialCircle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.socialCircle.dao.BlacklistDao;
import com.socialCircle.entity.Blacklist;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.BlacklistService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Resource
    private BlacklistDao dao;
    @Override
    public Result addBlacklist(Blacklist blacklist, User user) {
        if (blacklist.getBlack().equals(1)){
            return Result.error("官方账号不能拉黑");
        }
        if (blacklist.getBlack().equals(user.getId())){
            return Result.error("自己不能拉黑");
        }
        blacklist.setMe(user.getId());
        Blacklist bl = dao.selectOne(new QueryWrapper<Blacklist>()
                .eq("me", blacklist.getMe())
                .eq("black", blacklist.getBlack()));
        if (bl != null) {
            if (dao.deleteById(bl.getId()) == 1){
                return Result.ok("取消拉黑成功");
            }
            return Result.error("取消拉黑失败");
        }
        if (dao.insert(blacklist) == 1){
            return Result.ok("拉黑成功");
        }
        return Result.error("拉黑失败");
    }

    @Override
    public Result getBlacklistByUserId(Integer id) {
        List<Blacklist> me = dao.selectList(new QueryWrapper<Blacklist>()
                .eq("me", id));
        return Result.ok(me);
    }
}
