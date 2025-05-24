package com.music.common.user.controller;

import com.music.common.common.annotation.FrequencyControl;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.domain.vo.request.PlaylistSongPageReq;
import com.music.common.user.domain.vo.request.user.UserRegisterReq;
import com.music.common.user.domain.vo.request.user.UserSearchPageReq;
import com.music.common.user.domain.vo.request.user.UserUpdateReq;
import com.music.common.user.domain.vo.response.friend.FriendResp;
import com.music.common.user.domain.vo.response.user.UserInfoResp;
import com.music.common.user.domain.vo.response.user.UserLoginResp;
import com.music.common.user.service.IUserService;
import com.music.common.user.service.LoginService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "用户相关接口")
@RequestMapping("/capi/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户注册接口（手机号）
     */
    @PostMapping("/public/register")
    @ApiOperation("用户注册")
    public ApiResult<UserLoginResp> register(@RequestBody UserRegisterReq userInfo) {
        return ApiResult.success(userService.register(userInfo));
    }

    /**
     * 用户登录接口（手机号）
     */

    @GetMapping("/public/login")
    @ApiOperation("用户登录")
    public ApiResult<UserLoginResp> login(@RequestParam String phone) {
        return ApiResult.success(userService.login(phone));
    }

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
//    @FrequencyControl(time = 30, count = 3, target = FrequencyControl.Target.UID)
    public ApiResult<UserInfoResp> getUserInfo() {
        Long uid = RequestHolder.get().getUid();
        UserInfoResp userInfo = userService.getUserInfo(uid);
        return ApiResult.success(userInfo);
    }

    @GetMapping("/getUser")
    @ApiOperation("搜索用户")
    public ApiResult<PageBaseResp<FriendResp>> searchUser(@Valid UserSearchPageReq req) {
        return ApiResult.success(userService.searchUser(req));
    }

    @PostMapping("/update")
    @ApiOperation("修改个人信息")
    public ApiResult<Boolean> updateUser(@Valid @RequestBody UserUpdateReq req) {
        return ApiResult.success(userService.updateUser(req));
    }
}
