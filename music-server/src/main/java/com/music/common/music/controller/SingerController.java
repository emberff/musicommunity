package com.music.common.music.controller;


import cn.hutool.db.Page;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.domain.vo.reponse.*;
import com.music.common.music.service.ISingerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 歌手表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@RestController
@Api(tags = "歌手相关接口")
@RequestMapping("/capi/singer")
public class SingerController {
    @Autowired
    private ISingerService singerService;

    @GetMapping("/get")
    @ApiOperation("查询歌手详细信息")
    public ApiResult<SingerDetailResp> getSingerDetail(@Valid IdReqVO req) {
        SingerDetailResp singerDetail = singerService.getSingerDetail(req.getId());
        return ApiResult.success(singerDetail);
    }

    @GetMapping("/page")
    @ApiOperation("歌手分页")
    public ApiResult<PageBaseResp<SingerPageResp>> page(@Valid PageBaseReq req) {
        return ApiResult.success(singerService.pageSinger(req));
    }

    @PutMapping("/follow")
    @ApiOperation("关注歌手")
    public ApiResult<Boolean> followSinger(@Valid @RequestBody IdReqVO reqVO) {
        return ApiResult.success(singerService.followSinger(reqVO));
    }

    @GetMapping("/album/page")
    @ApiOperation("歌手专辑列表")
    public ApiResult<List<PlaylistDetailResp>> getAlbumPage(@Valid IdReqVO reqVO) {
        return ApiResult.success(singerService.getSingerAlbum(reqVO.getId()));
    }
}

