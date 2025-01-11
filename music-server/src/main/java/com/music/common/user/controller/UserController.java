package com.music.common.user.controller;

import com.music.common.common.domain.vo.resp.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/userInfo")
    public ApiResult<Object> getUserInfo() {
        return ApiResult.success();
    }
}
