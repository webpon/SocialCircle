package com.socialCircle.dao;

import com.socialCircle.entity.Hobby;

import java.util.List;

public interface HobbyDao {
    List<Hobby> getHobbies();

    boolean addHobby(Hobby hobby);

    boolean deleteHobby(List<Integer> ids);
}
