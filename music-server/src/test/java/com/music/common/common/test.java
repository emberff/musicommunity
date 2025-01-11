package com.music.common.common;

import com.music.common.MusicommunityApplication;
import com.music.common.common.utils.JwtUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = MusicommunityApplication.class)
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void jwt(){
        System.out.println(jwtUtils.createToken(1L));
        System.out.println(jwtUtils.createToken(2L));
        jwtUtils.createToken(2L);
    }
}
