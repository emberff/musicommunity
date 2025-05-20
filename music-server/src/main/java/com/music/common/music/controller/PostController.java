package com.music.common.music.controller;


import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.vo.reponse.*;
import com.music.common.music.domain.vo.request.*;
import com.music.common.music.service.IPostCommentService;
import com.music.common.music.service.IPostContentService;
import com.music.common.music.service.IPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;

/**
 * <p>
 * 发布作品表-参考tiktok 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
@RestController
@Api(tags = "发布帖子相关接口")
@RequestMapping("/capi/post")
public class PostController {

    @Autowired
    private IPostService postService;
    @Autowired
    private IPostCommentService postCommentService;


    @PostMapping("/add")
    @ApiOperation("新增发布")
    public ApiResult<Boolean> addPost(@Valid @RequestBody PostAddReq req) {
        return ApiResult.success(postService.addPost(req));
    }

    @GetMapping("/get")
    @ApiOperation("查询发布帖子详细信息")
    public ApiResult<PostResp> getSong(@Valid IdReqVO req) {
        PostResp postDetail = postService.getPostDetail(req.getId());
        return ApiResult.success(postDetail);
    }

    @GetMapping("/page")
    @ApiOperation("获取帖子分页")
    public ApiResult<PageBaseResp<PostPageResp>> getSongPage(@Valid PageBaseReq req) {
        return ApiResult.success(postService.getPostPage(req));
    }

    @PostMapping("/update")
    @ApiOperation("修改帖子")
    private ApiResult<Boolean> updatePost(@Valid @RequestBody PostUpdateReq req) {
        return ApiResult.success(postService.updatePost(req));
    }

    @PostMapping("/like")
    @ApiOperation("喜欢/取消喜欢post")
    private ApiResult<Boolean> likePost(@Valid @RequestBody IdReqVO idReqVO) {
        return ApiResult.success(postService.likePost(idReqVO));
    }

    @PostMapping("/postComment/add")
    @ApiOperation("评论帖子")
    private ApiResult<Boolean> commentPost(@Valid @RequestBody PostCommentAddReq req) {
        return ApiResult.success(postCommentService.addPostCpmment(req));
    }

    @PutMapping("/postComment/update")
    @ApiOperation("修改评论(集成adimn权限) 改为隐藏")
    private ApiResult<Void> updateComment(@Valid @RequestBody IdReqVO req) {
        postCommentService.updateComment(req);
        return ApiResult.success();
    }

    @GetMapping("/postComment/page")
    @ApiOperation("评论分页")
    private ApiResult<PageBaseResp<PostCommentPageRespVO>> page(@Valid PostCommentPageReq req) {
        return ApiResult.success(postCommentService.pagePostComment(req));
    }
}

