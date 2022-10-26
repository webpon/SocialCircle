package com.socialCircle.controller;

import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.Result;
import com.socialCircle.service.HobbyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/hobby")
public class HobbyController {

    @Resource
    private HobbyService hobbyService;

    @GetMapping
    public Result getHobbies(){
        return hobbyService.getHobbies();
    }

    @PostMapping
    public Result addHobby(@RequestBody Hobby hobby){
        return hobbyService.addHobby(hobby);
    }

    @DeleteMapping
    public Result deleteHobby(@RequestParam List<Integer> ids){
        return hobbyService.deleteHobby(ids);
    }

}
