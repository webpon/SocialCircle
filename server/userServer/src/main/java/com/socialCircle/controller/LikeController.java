package com.socialCircle.controller;

import com.socialCircle.entity.Like;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.LikeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Resource
    private LikeService likeService;

    @GetMapping("/dynamic")
    public Result getLikesByDynamicId(Integer dynamicId,
                                      @RequestParam(defaultValue = "1") Integer p){
        return likeService.getLikesByDynamicId(dynamicId, p);
    }

    @PostMapping("/dynamic")
    public Result likeByDynamicId(@RequestBody Like like,
                                  @RequestAttribute User user){
        return likeService.likeByDynamicId(like, user.getId());
    }

    @GetMapping("/comment")
    public Result getLikesByCommentId(Integer commentId,
                                      @RequestParam(defaultValue = "1") Integer p){
        return likeService.getLikesByCommentId(commentId, p);
    }

    @PostMapping("/comment")
    public Result likeByCommentId(@RequestBody Like like,
                                  @RequestAttribute User user){
        return likeService.likeByCommentId(like, user.getId());
    }

    @GetMapping("/dynamic/whether")
    public Result whetherLikeByDynamicId(Integer dynamicId,
                                      @RequestAttribute User user){
        return likeService.whetherLikeByDynamicId(dynamicId, user);
    }

    @GetMapping("/comment/whether")
    public Result whetherLikeByCommentId(Integer commentId,
                                      @RequestAttribute User user){
        return likeService.whetherLikeByCommentId(commentId, user);
    }
}
