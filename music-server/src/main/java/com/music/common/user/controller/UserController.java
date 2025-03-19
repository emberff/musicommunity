package com.music.common.user.controller;

import com.music.common.common.annotation.FrequencyControl;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.utils.RequestHolder;
import com.music.common.user.domain.vo.request.user.UserRegisterReq;
import com.music.common.user.domain.vo.response.user.UserInfoResp;
import com.music.common.user.domain.vo.response.user.UserLoginResp;
import com.music.common.user.service.IUserService;
import com.music.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/capi/user")
public class UserController {

    @Autowired
    private LoginService loginServicel;
    @Autowired
    private IUserService userService;

    /**
     * 用户注册接口（手机号）
     */
    @PostMapping("/public/register")
    public ApiResult<UserLoginResp> register(@RequestBody UserRegisterReq userInfo) {
        return ApiResult.success(userService.register(userInfo));
    }

    /**
     * 用户登录接口（手机号）
     */

    @GetMapping("/public/login")
    public ApiResult<UserLoginResp> login(@RequestParam String phone) {
        return ApiResult.success(userService.login(phone));
    }

    @GetMapping("/userInfo")
//    @FrequencyControl(time = 30, count = 3, target = FrequencyControl.Target.UID)
    public ApiResult<Object> getUserInfo() {
        Long uid = RequestHolder.get().getUid();
        UserInfoResp userInfo = userService.getUserInfo(uid);
        return ApiResult.success(userInfo);
    }
}
