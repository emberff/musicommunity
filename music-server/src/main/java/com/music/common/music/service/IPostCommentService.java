package com.music.common.music.service;

import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.entity.PostComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.vo.reponse.CommentPageRespVO;
import com.music.common.music.domain.vo.reponse.PostCommentPageRespVO;
import com.music.common.music.domain.vo.request.PostCommentAddReq;
import com.music.common.music.domain.vo.request.PostCommentPageReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
public interface IPostCommentService {

    Boolean addPostCpmment(PostCommentAddReq req);

    void updateComment(IdReqVO req);

    PageBaseResp<PostCommentPageRespVO> pagePostComment(PostCommentPageReq req);
}
