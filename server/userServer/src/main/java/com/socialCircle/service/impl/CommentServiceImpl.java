package com.socialCircle.service.impl;

import com.socialCircle.dao.CommentDao;
import com.socialCircle.entity.Comment;
import com.socialCircle.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;
    @Override
    public Comment getById(Integer commentId) {
        return commentDao.selectById(commentId);
    }
}
