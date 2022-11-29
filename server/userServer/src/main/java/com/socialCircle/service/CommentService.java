package com.socialCircle.service;

import com.socialCircle.entity.Comment;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;

public interface CommentService {
    Comment getById(Integer commentId);

    Result getComments(Integer dynamicId, Integer p, Long parentId);

    Result addComment(Comment comment, User user);

    Result deleteCommentById(Long commentId, User user);

    void deleteCommentByDynamicId(Integer id);
}
