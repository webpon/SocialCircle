package com.socialCircle.vm;

import com.socialCircle.entity.User;
import com.socialCircle.entity.UserInfo;
import com.socialCircle.entity.WorkExperience;
import lombok.Data;

@Data
public class UserInfoVM_ {
    private User user;
    private UserInfo userInfo;
    private WorkExperience workExperience;
}
