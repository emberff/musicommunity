package com.music.common.user.controller;

import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.utils.RequestHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/capi/user")
public class UserController {

    @GetMapping("/userInfo")
    public ApiResult<Object> getUserInfo() {
        String ip = RequestHolder.get().getIp();
        Long uid = RequestHolder.get().getUid();
        System.out.println(ip);
        System.out.println(uid);
        return ApiResult.success();
    }
}
