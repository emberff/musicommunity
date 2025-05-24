package com.music.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.user.domain.entity.User;
import com.music.common.user.domain.vo.request.user.UserRegisterReq;
import com.music.common.user.domain.vo.request.user.UserSearchPageReq;
import com.music.common.user.domain.vo.request.user.UserUpdateReq;
import com.music.common.user.domain.vo.response.friend.FriendResp;
import com.music.common.user.domain.vo.response.user.UserInfoResp;
import com.music.common.user.domain.vo.response.user.UserLoginResp;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-01-07
 */
public interface IUserService{

    /**
     * 用户注册
     * @param userInfo
     * @return
     */
    UserLoginResp register(UserRegisterReq userInfo);

    /**
     * 用户登录
     *
     * @param phone
     * @return
     */
    UserLoginResp login(String phone);

    /**
     * 获取前端展示信息
     *
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);

    Boolean isAdmin(Long userId);


    PageBaseResp<FriendResp> searchUser(UserSearchPageReq req);

    Boolean updateUser(UserUpdateReq req);
}
