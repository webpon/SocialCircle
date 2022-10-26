package com.socialCircle.service.impl;

import com.socialCircle.dao.UserHobbyDao;
import com.socialCircle.entity.UserHobby;
import com.socialCircle.service.UserHobbyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserHobbyServiceImpl implements UserHobbyService {

    @Resource
    private UserHobbyDao userHobbyDao;
    @Override
    public void deleteByHobbyId(List<Integer> ids) {
        userHobbyDao.deleteByHobbyId(ids);
    }
}
