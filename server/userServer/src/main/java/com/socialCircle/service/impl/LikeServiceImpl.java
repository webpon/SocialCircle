package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.LikeDao;
import com.socialCircle.entity.*;
import com.socialCircle.service.CommentService;
import com.socialCircle.service.DynamicService;
import com.socialCircle.service.LikeService;
import com.socialCircle.vm.DynamicVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static com.socialCircle.constant.RedisKey.*;

@Service
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeDao likeDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ResponseChatUtil chatUtil;
    @Resource
    private DynamicService dynamicService;
    @Resource
    private CommentService commentService;

    @Override
    public Result getLikesByDynamicId(Integer dynamicId, Integer p) {
        String key = dynamicId + ":" + p;
        // redis查询对象
        RedisQuery<List<DynamicVM>> query =
                new RedisQuery<>(LIKE_DYNAMIC_QUERY_KEY, key, null, DateField.MINUTE, 10);
        RedisCommand redisCommand = (k) -> {

            Page dynamicPage = new Page<>((p - 1) * 10, 10);
            QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dynamic_id", dynamicId);

            IPage page = likeDao.selectPage(dynamicPage, queryWrapper);
            List<Like> data = page.getRecords();

            RedisQuery<List<Like>> listRedisQuery =
                    new RedisQuery<>(LIKE_DYNAMIC_QUERY_KEY, k, data, DateField.MINUTE, 10);
            redisUtil.save(k, listRedisQuery);
        };
        List<Like> beans = redisUtil.getBeans(query, redisCommand, Like.class);
        if (beans == null) {
            return Result.error("没有数据");
        }
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dynamic_id", dynamicId);
        Integer integer = likeDao.selectCount(queryWrapper);

        Result<List<Like>> ok = Result.ok(beans);
        ok.setTotal(integer.longValue());
        return ok;
    }

    @Override
    public Result likeByDynamicId(Like like, Integer userId) {
        like.setUserId(userId);
        Like one = likeDao.selectOne(
                new QueryWrapper<Like>()
                        .eq("user_id", userId)
                        .eq("dynamic_id", like.getDynamicId())
        );
        // 是否有点赞
        if (one != null || one.getId() != null) {
            if (likeDao.deleteById(one.getId()) > 0) {
                dynamicService.subtractLikeNum(like.getDynamicId());
                return Result.ok("取消成功");
            }
            return Result.error("取消失败");
        }

        // 点赞
        like.setCreateTime(new Date());
        if (likeDao.insert(like) > 0) {
            redisUtil.batchDelete(LIKE_DYNAMIC_QUERY_KEY + like.getDynamicId());
            send(like);
            dynamicService.addLikeNum(like.getDynamicId());
            return Result.ok("点赞成功", like);
        }
        return Result.error("点赞失败");
    }

    @Override
    public Result getLikesByCommentId(Integer commentId, Integer p) {
        String key = commentId + ":" + p;
        // redis查询对象
        RedisQuery<List<DynamicVM>> query =
                new RedisQuery<>(LIKE_COMMENT_QUERY_KEY, key, null, DateField.MINUTE, 10);
        RedisCommand redisCommand = (k) -> {

            Page page = new Page<>((p - 1) * 10, 10);
            QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("comment_id", commentId);
            IPage iPage = likeDao.selectPage(page, queryWrapper);
            List<Like> data = iPage.getRecords();

            RedisQuery<List<Like>> listRedisQuery =
                    new RedisQuery<>(LIKE_COMMENT_QUERY_KEY, k, data, DateField.MINUTE, 10);
            redisUtil.save(k, listRedisQuery);
        };
        List<Like> beans = redisUtil.getBeans(query, redisCommand, Like.class);
        if (beans == null) {
            return Result.error("没有数据");
        }
        return Result.ok(beans);

    }

    @Override
    public Result likeByCommentId(Like like, Integer id) {
        like.setUserId(id);
        Like one = likeDao.selectOne(
                new QueryWrapper<Like>()
                        .eq("user_id", id)
                        .eq("comment_id", like.getCommentId())
        );
        // 是否有点赞
        if (one != null && one.getId() != null) {
            return likeDao.deleteById(one.getId()) > 0 ?
                    Result.ok("取消成功") :
                    Result.error("取消失败");
        }

        // 点赞
        like.setCreateTime(new Date());
        if (likeDao.insert(like) > 0) {
            redisUtil.batchDelete(LIKE_COMMENT_QUERY_KEY + like.getDynamicId());
            send(like);
            return Result.ok("点赞成功", like);
        }
        return Result.error("点赞失败");
    }

    @Override
    public Result whetherLikeByDynamicId(Integer dynamicId, User user) {
        Like like = likeDao.selectOne(new QueryWrapper<Like>()
                .eq("dynamic_id", dynamicId)
                .eq("user_id", user.getId())
        );
        if (like == null) {
            return Result.error(false);
        }
        return Result.ok(true);
    }

    @Override
    public Result whetherLikeByCommentId(Integer commentId, User user) {
        Like like = likeDao.selectOne(new QueryWrapper<Like>()
                .eq("comment_id", commentId)
                .eq("user_id", user.getId())
        );
        if (like == null) {
            return Result.error(false);
        }
        return Result.ok(true);
    }

    @Override
    public void deleteByDynamicId(Integer id) {
        likeDao.delete(new QueryWrapper<Like>().eq("dynamic_id", id));
    }

    @Override
    public void deleteByCommentId(Long commentId) {
        likeDao.delete(new QueryWrapper<Like>().eq("comment_id", commentId));
    }

    private void send(Like like) {
        LikeMsg likeMsg = new LikeMsg(like);
        Message message = new Message(likeMsg);
        message.setType("like");
        if (like.getDynamicId() != null) {
            DynamicVM vm = dynamicService.getDynamicById(like.getDynamicId());
            Integer userId = vm.getDynamic().getUserId();
            if (userId == 1) return;
            message.setTo(userId);
        } else {
            Integer userId = commentService.getById(like.getCommentId()).getUserId();
            if (userId == 1) return;
            message.setTo(userId);
        }
        String s = UUID.randomUUID().toString().substring(10);
        redisUtil.save(s, message);
        chatUtil.sendMsg(s);
    }
}
