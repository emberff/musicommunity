package com.music.common.oss.service;


import com.music.common.oss.domain.vo.OssResp;
import com.music.common.oss.domain.vo.UploadUrlReq;

/**
 * <p>
 * oss 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2023-03-19
 */
public interface OssService {

    /**
     * 获取临时的上传链接
     */
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}
