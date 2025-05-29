package com.music.common.music.service;

import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.entity.Singer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.reponse.PlaylistPageResp;
import com.music.common.music.domain.vo.reponse.SingerDetailResp;
import com.music.common.music.domain.vo.reponse.SingerPageResp;

import java.util.List;

/**
 * <p>
 * 歌手表 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
public interface ISingerService{

    SingerDetailResp getSingerDetail(Long id);

    PageBaseResp<SingerPageResp> pageSinger(PageBaseReq req);

    Boolean followSinger(IdReqVO reqVO);

    List<PlaylistDetailResp> getSingerAlbum(Long id);
}
