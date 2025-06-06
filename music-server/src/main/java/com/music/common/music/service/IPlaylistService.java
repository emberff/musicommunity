package com.music.common.music.service;

import com.music.common.common.domain.vo.req.IdListReqVO;
import com.music.common.common.domain.vo.req.IdReqVO;
import com.music.common.common.domain.vo.req.PageBaseReq;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.reponse.PlaylistPageResp;
import com.music.common.music.domain.vo.reponse.PlaylistSongPageResp;
import com.music.common.music.domain.vo.request.*;

import java.util.List;

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
    void addSongToPlaylist(SongToPlaylistReq req);

    /**
     * 在歌单删除歌曲
     * @param req
     */
    void deteteSongToPlaylist(SongToPlaylistReq req);

    /**
     * 用户关注的歌单信息分页
     * @param uid
     * @param req
     * @return
     */
    PageBaseResp<PlaylistPageResp> pagePlaylist(Long uid, PageBaseReq req);

    /**
     * 歌单内歌曲分页
     * @param req
     * @return
     */
    PageBaseResp<PlaylistSongPageResp> pagePlaylistSong(PlaylistSongPageReq req);

    /**
     * 无筛选的歌单分页
     * @param req
     * @return
     */
    PageBaseResp<PlaylistPageResp> pagePlaylist2(PageBaseReq req);

    /**
     * 用户关注歌单
     */
    Boolean followPlaylist(IdReqVO reqVO);

    PageBaseResp<PlaylistPageResp> pageRecPlaylist(Long uid, PageBaseReq req);

    Boolean inviteFriend(PlaylistInviteReq reqVO);

    List<Playlist> getManageList();

    PageBaseResp<Playlist> getFollowPlaylist(PageBaseReq req);
}
