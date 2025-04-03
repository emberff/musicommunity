package com.music.common.user.service.impl;

import com.music.common.common.constant.RedisKey;
import com.music.common.common.exception.BusinessException;
import com.music.common.common.utils.RedisUtils;
import com.music.common.music.dao.PlaylistDao;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.enums.IsPublicEnum;
import com.music.common.music.domain.enums.PlayListTypeEnum;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.domain.vo.request.user.UserRegisterReq;
import com.music.common.user.domain.vo.response.user.UserInfoResp;
import com.music.common.user.domain.vo.response.user.UserLoginResp;
import com.music.common.user.service.IUserService;
import com.music.common.user.service.LoginService;
import com.music.common.user.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;
    @Autowired
    private PlaylistDao playlistDao;

    @Override
    public UserLoginResp register(UserRegisterReq userInfo) {
        User byPhone = userDao.getByPhone(userInfo.getPhone());
        if (byPhone != null) {
            throw new BusinessException("手机号已使用!");
        }
        User user = User.builder()
                .phone(userInfo.getPhone())
                .name(userInfo.getName())
                .sex(userInfo.getSex())
                .avatar(userInfo.getAvatar())
                .build();
        userDao.save(user);

        Playlist playlist = Playlist.builder()
                .type(PlayListTypeEnum.USER_FAVOURITE.getValue())
                .isPublic(IsPublicEnum.IS_PUBLIC.getValue())
                .plCreatorId(user.getId())
                .build();
        playlistDao.save(playlist);

        String token = loginService.login(user.getId());
        UserLoginResp userLoginResp = new UserLoginResp();
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    @Override
    public UserLoginResp login(String phone) {
        User user = userDao.getByPhone(phone);
        if (user == null) {
            throw new BusinessException("未查询到用户信息, 请注册");
        }
        String token = RedisUtils.getStr(getUserTokenKey(user.getId()));
        UserLoginResp userLoginResp = new UserLoginResp();
        if (StringHelper.isNullOrEmptyString(token)) {
            token = loginService.login(user.getId());
            userLoginResp.setToken(token);
            return userLoginResp;
        }
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }


    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User userInfo = userDao.getById(uid);
        return UserAdapter.buildUserInfoResp(userInfo);
    }


}
