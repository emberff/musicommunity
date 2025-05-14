package com.music.common.music.controller;


import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.vo.reponse.CommentPageRespVO;
import com.music.common.music.domain.vo.request.CommentAddReq;
import com.music.common.music.domain.vo.request.CommentPageReq;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-14
 */
@RestController
@Api(tags = "歌曲评论相关接口")
@RequestMapping("/capi/comment")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @PostMapping("/add")
    @ApiOperation("新建评论")
    public ApiResult<Void> addComment(@Valid @RequestBody CommentAddReq req) {
        commentService.addComment(req);
        return ApiResult.success();
    }

    @PutMapping("/update")
    @ApiOperation("修改评论(集成adimn权限)")
    private ApiResult<Void> updateComment(@Valid @RequestBody IdReqVO req) {
        commentService.updateComment(req);
        return ApiResult.success();
    }

    @GetMapping("/page")
    @ApiOperation("评论分页")
    private ApiResult<PageBaseResp<CommentPageRespVO>> page(@Valid @RequestBody CommentPageReq req) {
        return ApiResult.success(commentService.pageComment(req));
    }

}

