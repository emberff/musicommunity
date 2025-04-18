package com.music.common.music.controller;


import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.service.ISongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 歌曲表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@RestController
@Api(tags = "歌曲相关接口")
@RequestMapping("/song")
public class SongController {
    @Autowired
    private ISongService songService;

    @GetMapping("/get")
    @ApiOperation("查询歌曲详细信息")
    public ApiResult<SongDetailResp> getSong(@Valid IdReqVO req) {
        SongDetailResp songDetail = songService.getSongDetail(req.getId());
        return ApiResult.success(songDetail);
    }

    //TODO 1.歌曲(用户自制)增删改接口, 上传文件接口需要开启oss服务 2.系统快捷添加歌曲(待探讨)
}

