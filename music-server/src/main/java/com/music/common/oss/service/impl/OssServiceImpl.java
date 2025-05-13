package com.music.common.oss.service.impl;

import com.music.common.common.utils.AssertUtil;
import com.music.common.oss.common.config.MinIOTemplate;
import com.music.common.oss.domain.enums.OssSceneEnum;
import com.music.common.oss.domain.vo.OssReq;
import com.music.common.oss.domain.vo.OssResp;
import com.music.common.oss.domain.vo.UploadUrlReq;
import com.music.common.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-06-20
 */
@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private MinIOTemplate minIOTemplate;

    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        OssSceneEnum sceneEnum = OssSceneEnum.of(req.getScene());
        AssertUtil.isNotEmpty(sceneEnum, "场景有误");
        OssReq ossReq = OssReq.builder()
                .fileName(req.getFileName())
                .filePath(sceneEnum.getPath())
                .uid(uid)
                .build();
        return minIOTemplate.getPreSignedObjectUrl(ossReq);
    }
}
