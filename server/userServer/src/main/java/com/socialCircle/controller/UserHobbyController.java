package com.socialCircle.controller;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.entity.UserHobby;
import com.socialCircle.service.UserHobbyService;
import com.socialCircle.vm.AddHobby;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/userHobby")
public class UserHobbyController {

    @Resource
    private UserHobbyService userHobbyService;

    @PostMapping
    public Result addUserHobby(@RequestBody AddHobby addHobby,
                               @RequestAttribute User user){
        return userHobbyService.addUserHobby(addHobby, user);
    }

    @DeleteMapping
    public Result deleteUserHobby(Integer hobbyId,
                                  @RequestAttribute User user){
        return userHobbyService.deleteUserHobby(hobbyId, user);
    }
//
//    @GetMapping
//    public Result getUserHobbies(@RequestAttribute User user){
//        return userHobbyService.getUserHobbies(user.getId());
//    }
}
