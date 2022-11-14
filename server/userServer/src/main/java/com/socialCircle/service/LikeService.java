package com.socialCircle.service;

import com.socialCircle.entity.Like;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;

public interface LikeService {
    Result getLikesByDynamicId(Integer dynamicId, Integer p);

    Result likeByDynamicId(Like like, Integer userId);

    Result getLikesByCommentId(Integer commentId, Integer p);

    Result likeByCommentId(Like like, Integer id);

    Result whetherLikeByDynamicId(Integer dynamicId, User user);

    Result whetherLikeByCommentId(Integer commentId, User user);
}
