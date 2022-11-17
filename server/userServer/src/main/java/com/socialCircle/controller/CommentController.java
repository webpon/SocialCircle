package com.socialCircle.controller;

import com.socialCircle.entity.Comment;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping
    public Result getComments(Integer dynamicId,
                              @RequestParam(defaultValue = "0") Long parentId,
                              @RequestParam(defaultValue = "1") Integer p
    ){
        return commentService.getComments(dynamicId,p, parentId);
    }

    @PostMapping
    public Result addComment(@RequestBody Comment comment,
                             @RequestAttribute User user){
        return commentService.addComment(comment, user);
    }

    @DeleteMapping
    public Result deleteComment(Integer commentId,
                             @RequestAttribute User user){
        return commentService.deleteCommentById(commentId.longValue(), user);
    }
}
