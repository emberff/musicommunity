package com.music.common.user.controller;

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
    public ApiResult<Object> getUserInfo() {
        String ip = RequestHolder.get().getIp();
        Long uid = RequestHolder.get().getUid();
        // 将 ip 和 uid 存入 Map
        Map<String, Object> data = new HashMap<>();
        data.put("ip", ip);
        data.put("uid", uid);
        return ApiResult.success(data);
    }
}
