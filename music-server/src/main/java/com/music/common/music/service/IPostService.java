package com.music.common.music.service;

import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.vo.reponse.PostPageResp;
import com.music.common.music.domain.vo.reponse.PostResp;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.request.PostAddReq;
import com.music.common.music.domain.vo.request.PostUpdateReq;

/**
 * <p>
 * 发布作品表-参考tiktok 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-20
 */
public interface IPostService{


    Boolean addPost(PostAddReq req);

    PostResp getPostDetail(Long id);

    PageBaseResp<PostPageResp> getPostPage(PageBaseReq req);

    Boolean updatePost(PostUpdateReq req);

    Boolean likePost(IdReqVO idReqVO);

    PageBaseResp<PostPageResp> getLikePostPage(PageBaseReq req);
}
