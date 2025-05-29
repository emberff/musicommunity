package com.music.common.music.controller;

import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.domain.vo.reponse.UserSongPageResp;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.SongAddReq;
import com.music.common.music.domain.vo.request.SongUpdateReq;
import com.music.common.music.service.ISongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
@RequestMapping("/capi/song")
public class SongController {
    @Autowired
    private ISongService songService;

    @GetMapping("/get")
    @ApiOperation("查询歌曲详细信息")
    public ApiResult<SongDetailResp> getSong(@Valid IdReqVO req) {
        SongDetailResp songDetail = songService.getSongDetail(req.getId());
        return ApiResult.success(songDetail);
    }

    @GetMapping("/page")
    @ApiOperation("获取歌曲分页")
    public ApiResult<PageBaseResp<SimpleSongListResp>> getSongPage(@Valid PageBaseReq req) {
         return ApiResult.success(songService.getSongPage(req));
    }

    @PostMapping("/add")
    @ApiOperation("新增歌曲")
    public ApiResult<Boolean> addSong(@Valid @RequestBody SongAddReq req) {
        return ApiResult.success(songService.saveSong(req));
    }

    @PostMapping("update")
    @ApiOperation("修改歌曲")
    private ApiResult<Boolean> updateSong(@Valid @RequestBody SongUpdateReq req) {
        return ApiResult.success(songService.updateSong(req));
    }

    @GetMapping("/user/page")
    @ApiOperation("用户上传音乐分页")
    private ApiResult<PageBaseResp<UserSongPageResp>> getUserMusicPage(@Valid PageBaseReq req) {
        return ApiResult.success(songService.getUserMusicPage(req));
    }
}
