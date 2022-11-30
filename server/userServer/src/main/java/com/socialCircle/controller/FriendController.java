package com.socialCircle.controller;

import com.socialCircle.entity.FansConcern;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.FriendServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class FriendController {
    @Resource
    private FriendServer friendServer;

    @PostMapping("/concern")
    public Result concern(@RequestBody FansConcern fansConcern,
                          @RequestAttribute User user){
        return friendServer.concern(fansConcern,user);
    }

    @GetMapping("/concern")
    public Result concern(Integer p, String q,
                          @RequestAttribute User user){
        return friendServer.getConcern(q,p,user);
    }

    @GetMapping("/friend")
    public Result getFriend(Integer p, String q,
                          @RequestAttribute User user){
        return friendServer.getFriend(q,p,user);
    }

    @GetMapping("/fens")
    public Result getFens(Integer p, String q,
                          @RequestAttribute User user){
        return friendServer.getFens(q,p,user);
    }

}
