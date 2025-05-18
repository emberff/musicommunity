package com.music.common.music.controller;


import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.domain.vo.reponse.PlaylistPageResp;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.reponse.SingerDetailResp;
import com.music.common.music.service.IPlaylistService;
import com.music.common.music.service.ISingerService;
import com.music.common.music.service.ISongService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 推荐 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@RestController
@Api(tags = "推荐相关接口")
@RequestMapping("/capi/rec")
public class RecController {
    @Autowired
    private ISongService songService;
    @Autowired
    private IPlaylistService playlistService;
    @Autowired
    private ISingerService singerService;

    @GetMapping("/song/page")
    @ApiOperation("获取歌曲推荐")
    public ApiResult<PageBaseResp<SimpleSongListResp>> getSongPage(@Valid PageBaseReq req) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(songService.getSongRecPage(uid, req));
    }

//    @GetMapping("/playlist/page")
//    @ApiOperation("用户歌单推荐")
//    public ApiResult<PageBaseResp<PlaylistPageResp>> getPlaylistPage(@Valid PageBaseReq req) {
//        Long uid = RequestHolder.get().getUid();
//        return ApiResult.success(playlistService.pageRecPlaylist(uid, req));
//    }
//
//    @GetMapping("/singer/page")
//    @ApiOperation("用户歌单分页")
//    public ApiResult<PageBaseResp<PlaylistPageResp>> getSingerPage(@Valid PageBaseReq req) {
//        Long uid = RequestHolder.get().getUid();
//        return ApiResult.success(singerService.pageRecSinger(uid, req));
//    }

}

