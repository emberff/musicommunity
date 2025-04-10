package com.music.common.music.controller;


import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.reponse.PlaylistPageResp;
import com.music.common.music.domain.vo.reponse.PlaylistSongPageResp;
import com.music.common.music.domain.vo.request.PlaylistSongPageReq;
import com.music.common.music.domain.vo.request.SongToPlaylistReq;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import com.music.common.music.service.IPlaylistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 歌单表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-20
 */
@RestController
@Api(tags = "歌单相关接口")
@RequestMapping("/capi/playlist")
public class PlaylistController {

    @Autowired
    private IPlaylistService playlistService;

    @PostMapping("/add")
    @ApiOperation("新建歌单")
    public ApiResult<Void> addPlaylist(@Valid @RequestBody PlaylistAddReq req) {
        playlistService.addPlaylist(req);
        return ApiResult.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除歌单")
    public ApiResult<Void> deletePlaylist(@Valid @RequestBody IdReqVO req) {
        playlistService.detetePlaylist(req.getId());
        return ApiResult.success();
    }

    @PutMapping("/update")
    @ApiOperation("修改歌单信息")
    public ApiResult<Void> updatePlaylist(@Valid @RequestBody PlaylistUpdateReq req) {
        playlistService.updatePlaylist(req);
        return ApiResult.success();
    }

    @GetMapping("/get")
    @ApiOperation("查询歌单详细信息")
    public ApiResult<PlaylistDetailResp> getPlaylist(@Valid IdReqVO req) {
        return ApiResult.success(playlistService.getPlaylistDetail(req.getId()));
    }

    @GetMapping("/page")
    @ApiOperation("用户歌单分页")
    public ApiResult<PageBaseResp<PlaylistPageResp>> page(@Valid PageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(playlistService.pagePlaylist(uid, req));
    }

    @PutMapping("/addSong")
    @ApiOperation("添加歌曲")
    public ApiResult<Void> addSongToPlaylist(@Valid @RequestBody SongToPlaylistReq req) {
        playlistService.addSongToPlaylist(req);
        return ApiResult.success();
    }

    @DeleteMapping("/deleteSong")
    @ApiOperation("删除歌单内歌曲")
    public ApiResult<Void> deletePlaylistSong(@Valid @RequestBody SongToPlaylistReq req) {
        playlistService.deteteSongToPlaylist(req);
        return ApiResult.success();
    }

    @GetMapping("/song/page")
    @ApiOperation("用户歌单分页")
    public ApiResult<PageBaseResp<PlaylistSongPageResp>> page(@Valid PlaylistSongPageReq req) {
        return ApiResult.success(playlistService.pagePlaylistSong(req));
    }

}

