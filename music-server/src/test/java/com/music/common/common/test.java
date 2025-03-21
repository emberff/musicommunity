package com.music.common.common;

import com.music.common.MusicommunityApplication;
import com.music.common.common.thread.MyUncaughtExceptionHandler;
import com.music.common.common.utils.JwtUtils;
import com.music.common.common.utils.RedisUtils;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.service.IPlaylistService;
import com.music.common.user.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = MusicommunityApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class test {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void jwt(){
        System.out.println(jwtUtils.createToken(1L));
        System.out.println(jwtUtils.createToken(2L));
        jwtUtils.createToken(2L);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redis() {
        RedisUtils.set("name","卷心菜");
        String name = RedisUtils.get("name");
        System.out.println(name); //卷心菜
    }

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redisson() {
        RLock lock = redissonClient.getLock("123");
        lock.lock();
        System.out.println();
        lock.unlock();
    }

    @Autowired
    private LoginService loginService;
    @Test
    public void token(){
        String token = loginService.login(11001L);
        System.out.println(token);
        System.out.println(loginService.getValidUid(token));
    }

    @Test
    public void thread() throws InterruptedException {
        Thread thread = new Thread(() -> {
            if (1 == 1) {
                log.error("123");
                throw new RuntimeException("123");
            }
        });
        thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        thread.start();
        Thread.sleep(200);
    }

    @Autowired
    private IPlaylistService playlistServicel;
    @Test
    public void musiclist() {
        PlaylistAddReq playlistAddReq = new PlaylistAddReq();
        playlistAddReq.setName("test");
        playlistAddReq.setCover("123");
        playlistAddReq.setIsPublic(1);
        playlistServicel.addMusiclist(playlistAddReq);
    }
}
