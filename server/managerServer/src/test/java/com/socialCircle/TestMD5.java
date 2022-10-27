package com.socialCircle;

import cn.hutool.crypto.SecureUtil;
import com.socialCircle.entity.User;
import org.junit.Test;

public class TestMD5 {
    @Test
    public void getMd5(){
        User user = new User();
        user.setPassword("admin123456");
        md5(user);
        System.out.println(user.getPassword());
    }
    private void md5(User user){
        // 密码加密5次
        for (int i = 0; i < 5; i++) {
            String password = user.getPassword();
            String md5 = SecureUtil.md5(password);
            user.setPassword(md5);
        }
    }

}
