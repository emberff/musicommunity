package com.music.common.user.service.impl;

import com.music.common.common.constant.RedisKey;
import com.music.common.common.utils.JwtUtils;
import com.music.common.common.utils.RedisUtils;
import com.music.common.user.service.LoginService;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEWAL_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean verify(String token) {
        return false;
    }

    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        if (expireDays == -2) { // 不存在的kty
            return;
        }
        if (expireDays < TOKEN_RENEWAL_DAYS) {
            RedisUtils.expire(getUserTokenKey(uid), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if(Objects.isNull(uid)) {
            return null;
        }
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if (StringUtils.isBlank(oldToken)) {
            return null;
        }
        // 在另一端登陆后会重新添加一个token并刷新redis中缓存的token, 而oldToken仍能解析出uid,
        // 此处目的是保持token的是最新的值
        return Objects.equals(oldToken, token) ? uid : null;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }
}
