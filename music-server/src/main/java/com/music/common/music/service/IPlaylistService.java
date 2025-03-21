package com.music.common.music.service;

import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.request.AddSongToPlaylistReq;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;

import javax.validation.Valid;

/**
 * <p>
 * 歌单表 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-20
 */
public interface IPlaylistService{

    /**
     * 新建歌单
     * @param req
     */
    void addPlaylist(PlaylistAddReq req);

    /**
     * 修改歌单信息
     * @param req
     */
    void updatePlaylist(PlaylistUpdateReq req);

    /**
     * 删除歌单
     * @param playlistId
     */
    void detetePlaylist(Long playlistId);

    /**
     * 获取歌单信息, 包括歌单中歌曲的简单信息
     * @param playlistId
     * @return
     */
    PlaylistDetailResp getPlaylistDetail(Long playlistId);

    /**
     * 在歌单添加歌曲
     * @param req
     */
    void addSongToPlaylist(AddSongToPlaylistReq req);
}
