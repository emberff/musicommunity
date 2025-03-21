package com.music.common.music.controller;


import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.request.AddSongToPlaylistReq;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import com.music.common.music.service.IPlaylistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
@RequestMapping("/playlist")
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

    @PutMapping("/addSong")
    @ApiOperation("添加歌曲")
    public ApiResult<Void> addSongToPlaylist(@Valid @RequestBody AddSongToPlaylistReq req) {
        playlistService.addSongToPlaylist(req);
        return ApiResult.success();
    }

    //TODO 1.歌单删除歌曲 2.歌单列表分页
}

