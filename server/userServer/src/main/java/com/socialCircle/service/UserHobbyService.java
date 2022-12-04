package com.socialCircle.service;

import com.socialCircle.entity.Hobby;
import com.socialCircle.entity.Result;
import com.socialCircle.entity.User;
import com.socialCircle.entity.UserHobby;
import com.socialCircle.vm.AddHobby;

import java.util.List;

public interface UserHobbyService {
    List<UserHobby> getUserHobbiesByUserId(Integer id);

    Result addUserHobby(AddHobby addHobby, User user);

    Result deleteUserHobby(Integer hobbyId, User user);

}
