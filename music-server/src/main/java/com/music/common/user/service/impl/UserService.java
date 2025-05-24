package com.music.common.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.constant.RedisKey;
import com.music.common.common.domain.enums.UserTypeEnum;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.exception.BusinessException;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RedisUtils;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.PlaylistDao;
import com.music.common.music.dao.PowerDao;
import com.music.common.music.dao.UserPlaylistDao;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.Power;
import com.music.common.music.domain.entity.UserPlaylist;
import com.music.common.music.domain.enums.IsPublicEnum;
import com.music.common.music.domain.enums.PlayListTypeEnum;
import com.music.common.music.domain.enums.PowerTypeEnum;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.domain.vo.request.user.UserRegisterReq;
import com.music.common.user.domain.vo.request.user.UserSearchPageReq;
import com.music.common.user.domain.vo.request.user.UserUpdateReq;
import com.music.common.user.domain.vo.response.friend.FriendResp;
import com.music.common.user.domain.vo.response.user.UserInfoResp;
import com.music.common.user.domain.vo.response.user.UserLoginResp;
import com.music.common.user.service.IUserService;
import com.music.common.user.service.LoginService;
import com.music.common.user.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;
    @Autowired
    private PlaylistDao playlistDao;
    @Autowired
    private UserPlaylistDao userPlaylistDao;
    @Autowired
    private PowerDao powerDao;

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
                .name(user.getName() + "喜欢的音乐")
                .plCreatorId(user.getId())
                .build();
        playlistDao.save(playlist);

        UserPlaylist userPlaylist = UserPlaylist.builder()
                        .playlistId(playlist.getId())
                        .userId(user.getId())
                        .build();
        userPlaylistDao.save(userPlaylist);

        Power power = Power.builder()
                .userId(user.getId())
                .powerType(PowerTypeEnum.CREATOR.getValue())
                .playlistId(playlist.getId())
                .build();
        powerDao.save(power);

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

    @Override
    public Boolean isAdmin(Long userId) {
        User user = userDao.getById(userId);
        return user.getType().equals(UserTypeEnum.Admin.getValue());

    }

    @Override
    public PageBaseResp<FriendResp> searchUser(UserSearchPageReq req) {
        Page<User> userPage = userDao.searchUser(req);
        List<User> users = userPage.getRecords();
        List<FriendResp> resps = users.stream()
                .map(user -> {
                    FriendResp resp = new FriendResp();
                    BeanUtil.copyProperties(user, resp); // 复制其他字段
                    resp.setUid(user.getId()); // 映射 id → uid
                    return resp;
                })
                .collect(Collectors.toList());

        return PageBaseResp.init(userPage, resps);
    }

    @Override
    public Boolean updateUser(UserUpdateReq req) {
        Long uid = RequestHolder.get().getUid();
        User user = userDao.getById(uid);
        AssertUtil.isNotEmpty(user, "未找到当前用户!");
        if (req.getSex() != null){
            user.setSex(req.getSex());
        }
        if (req.getName() != null){
            user.setName(req.getName());
        }
        if (req.getPhone() != null) {
            user.setPhone(req.getPhone());
        }
        if (req.getAvatar() != null) {
            user.setAvatar(req.getAvatar());
        }
        userDao.save(user);
        return true;
    }


}
