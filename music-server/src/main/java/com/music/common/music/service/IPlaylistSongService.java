package com.music.common.music.service;

import com.music.common.music.domain.vo.reponse.SingerDetailResp;

import java.util.List;

/**
 * <p>
 * 歌单歌曲关联表 服务类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
public interface IPlaylistSongService {

    void addSongsToPlaylist(Long playlistId, List<Long> songIds);
}
