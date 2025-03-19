package com.music.common.user.service.impl;

import com.music.common.common.exception.BusinessException;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.domain.vo.request.user.UserRegisterReq;
import com.music.common.user.domain.vo.response.user.UserInfoResp;
import com.music.common.user.domain.vo.response.user.UserLoginResp;
import com.music.common.user.service.IUserService;
import com.music.common.user.service.LoginService;
import com.music.common.user.service.adapter.UserAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;

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
                .avatar(userInfo.getAvatar()).build();
        userDao.save(user);

        Long uid = userDao.getByPhone(userInfo.getPhone()).getId();
        String token = loginService.login(uid);
        UserLoginResp userLoginResp = new UserLoginResp();
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    @Override
    public UserLoginResp login(String phone) {
        User byPhone = userDao.getByPhone(phone);
        if (byPhone == null) {
            throw new BusinessException("未查询到用户信息, 请注册");
        }
        String token = loginService.login(byPhone.getId());
        UserLoginResp userLoginResp = new UserLoginResp();
        userLoginResp.setToken(token);
        return userLoginResp;
    }


    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User userInfo = userDao.getById(uid);
        return UserAdapter.buildUserInfoResp(userInfo);
    }


}
