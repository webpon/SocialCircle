package com.socialCircle.controller;

import com.socialCircle.entity.Blacklist;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.service.BlacklistService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blacklist")
public class BlacklistController {
    @Resource
    private BlacklistService service;

    @PostMapping
    public Result addBlacklist(@RequestBody Blacklist blacklist,
                               @RequestAttribute User user){
        return service.addBlacklist(blacklist, user);
    }

    @GetMapping
    public Result getBlacklist(@RequestAttribute User user){
        return service.getBlacklistByUserId(user.getId());
    }
}

