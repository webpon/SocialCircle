package com.socialCircle.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.socialCircle.common.RedisUtil;
import com.socialCircle.common.ResponseChatUtil;
import com.socialCircle.constant.RedisCommand;
import com.socialCircle.constant.RedisQuery;
import com.socialCircle.dao.CommentDao;
import com.socialCircle.entity.*;
import com.socialCircle.msg.CommentMsg;
import com.socialCircle.msg.Message;
import com.socialCircle.service.CommentService;
import com.socialCircle.service.DynamicService;
import com.socialCircle.service.LikeService;
import com.socialCircle.vm.CommentMO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.socialCircle.constant.RedisKey.COMMENT_QUERY_KEY;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private DynamicService dynamicService;
    @Resource
    private ResponseChatUtil chatUtil;
    @Resource
    private LikeService likeService;

    @Override
    public Comment getById(Integer commentId) {
        return commentDao.selectById(commentId);
    }

    @Override
    public Result getComments(Integer dynamicId, Integer p, Long parentId) {
        List<CommentMO> beans = getCommentList(dynamicId, p, parentId);
        if (beans == null) {
            return Result.error("没有数据");
        }
        return Result.ok(beans);
    }

    @Override
    public Result addComment(Comment comment, User user) {
        comment.setUserId(user.getId());
        comment.setCreateTime(new Date());
        if (commentDao.insert(comment) > 0) {
            // 发送评论信息
            CommentMsg commentMsg = new CommentMsg(comment);
            Dynamic dynamic = dynamicService.getDynamicById(comment.getDynamicId()).getDynamic();
            Integer userId = dynamic.getUserId();
            if (userId != 1) {
                commentMsg.setTo(userId);
                commentMsg.setForm(user.getId());
                Message<CommentMsg> message = new Message<>(commentMsg);
                message.setType("comment");
                String s = UUID.randomUUID().toString().substring(10);
                redisUtil.save(s, message);
                chatUtil.sendMsg(s);
            }
            dynamicService.addCommentNum(dynamic.getId(), 1);
            // 删除缓存
            redisUtil.batchDelete(COMMENT_QUERY_KEY + comment.getDynamicId());
            return Result.ok(commentDao.selectById(comment.getId()));
        }
        return Result.error("发布失败");
    }

    @Override
    public Result deleteCommentById(Long commentId, User user) {
        Comment id = commentDao.selectOne(new QueryWrapper<Comment>().eq("id", commentId));
        if (id == null || !id.getUserId().equals(user.getId())) {
            return Result.error();
        }
        if (commentDao.deleteById(commentId) > 0) {
            QueryWrapper<Comment> eq = new QueryWrapper<Comment>()
                    .eq("parent_id", commentId);
            int delete = commentDao.delete(eq);

            dynamicService.subtractCommentNum(id.getDynamicId(), delete + 1);
            likeService.deleteByCommentId(commentId);
            return Result.ok("删除成功");
        }
        return Result.error();
    }

    @Override
    public void deleteCommentByDynamicId(Integer id) {
        commentDao.delete(new QueryWrapper<Comment>().eq("dynamic_id", id));
    }

    private List<CommentMO> getCommentList(Integer dynamicId, Integer p, Long parentId) {
        String key = dynamicId + ":" + p;
        key += ":" + parentId;
        // redis查询对象
        RedisQuery<List<CommentMO>> query =
                new RedisQuery<>(COMMENT_QUERY_KEY, key, null, DateField.MINUTE, 20);
        RedisCommand<List<CommentMO>> redisCommand = (k) -> {
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dynamic_id", dynamicId);
            queryWrapper.eq("parent_id", parentId);
            queryWrapper.orderByDesc("like_num");
            List<Comment> data;
            if (p != null) {
                // 不是空就分页查询
                data = commentDao.selectList(queryWrapper.last("limit "+(p - 1) * 10+","+10));
            } else {
                data = commentDao.selectList(queryWrapper);
            }

            ArrayList<CommentMO> commentMOS = new ArrayList<>();
            if (data != null) {
                data.forEach(comment -> {
                    // 转换数据模型
                    CommentMO commentMO = new CommentMO(comment);
                    // 查询
                    commentMO.setChildList(getCommentList(dynamicId, null, comment.getId()));
                    commentMOS.add(commentMO);
                });
            }
            return commentMOS;
        };
        return redisUtil.getBeans(query, redisCommand, CommentMO.class);
    }
}
