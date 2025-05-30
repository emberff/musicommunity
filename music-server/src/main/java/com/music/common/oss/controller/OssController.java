package com.music.common.oss.controller;

import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.utils.RequestHolder;
import com.music.common.oss.domain.vo.OssResp;
import com.music.common.oss.domain.vo.UploadUrlReq;
import com.music.common.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Description: oss控制层
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-06-20
 */
@RestController
@RequestMapping("/capi/oss")
@Api(tags = "oss相关接口")
public class OssController {
    @Autowired
    private OssService ossService;

    /**
     * 上传成功后直接将downloadUrl入库, 而不是对某一文件重新请求接口再调用
     * @param req
     * @return
     */
    @GetMapping("/public/upload/url")
    @ApiOperation("获取临时上传链接")
    public ApiResult<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
        return ApiResult.success(ossService.getUploadUrl(RequestHolder.get().getUid(), req));
    }
}
