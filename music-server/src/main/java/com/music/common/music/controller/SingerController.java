package com.music.common.music.controller;


import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.music.domain.vo.reponse.SingerDetailResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.service.ISingerService;
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
 * 歌手表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@RestController
@Api(tags = "歌手相关接口")
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    private ISingerService singerService;

    @GetMapping("/get")
    @ApiOperation("查询歌手详细信息")
    public ApiResult<SingerDetailResp> getSingerDetail(@Valid IdReqVO req) {
        SingerDetailResp singerDetail = singerService.getSingerDetail(req.getId());
        return ApiResult.success(singerDetail);
    }

    //TODO 1.歌手表字段 有待完善 2.用户关注歌手表未建 3.其他相关功能待探讨
}

