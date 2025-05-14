package com.music.common.music.service;

import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.ApiResult;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.vo.reponse.CommentPageRespVO;
import com.music.common.music.domain.vo.request.CommentAddReq;
import com.music.common.music.domain.vo.request.CommentPageReq;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-14
 */
public interface ICommentService {

    void addComment(CommentAddReq req);

    void updateComment(IdReqVO req);

    PageBaseResp<CommentPageRespVO> pageComment(CommentPageReq req);
}
