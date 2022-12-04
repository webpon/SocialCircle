package com.socialCircle.service;

import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.Result;

import java.util.List;

public interface HobbyService {
    Result getHobbies();

    List<Hobby> getHobbiesByIds(List<Integer> list);
}
