package com.socialCircle.controller;

import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.entity.WorkExperience;
import com.socialCircle.service.WorkExperienceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/workExperience")
public class WorkExperienceController {

    @Resource
    private WorkExperienceService workExperienceService;

    @PostMapping
    public Result addWorkExperience(@RequestAttribute User user,
                                    @RequestBody WorkExperience workExperience){
        return workExperienceService.addWorkExperience(user, workExperience);
    }
    
    @DeleteMapping
    public Result deleteWorkExperience(Integer id, @RequestAttribute User user){
        return workExperienceService.deleteWorkExperience(id, user.getId());
    }
}
