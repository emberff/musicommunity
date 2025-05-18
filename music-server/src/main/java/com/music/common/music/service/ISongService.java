package com.music.common.music.service;

import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.domain.vo.request.SongAddReq;
import com.music.common.music.domain.vo.request.SongUpdateReq;

/**
 * <p>
 * 歌曲表 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
public interface ISongService{

    SongDetailResp getSongDetail(Long id);

    PageBaseResp<SimpleSongListResp> getSongPage(PageBaseReq req);

    Boolean saveSong(SongAddReq req);

    Boolean updateSong(SongUpdateReq req);
}
