package com.music.common.music.service.impl;

import com.music.common.common.exception.BusinessException;
import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.PlaylistDao;
import com.music.common.music.dao.PlaylistSongDao;
import com.music.common.music.dao.PowerDao;
import com.music.common.music.dao.UserPlaylistDao;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.PlaylistSong;
import com.music.common.music.domain.entity.Power;
import com.music.common.music.domain.entity.UserPlaylist;
import com.music.common.music.domain.enums.IsPublicEnum;
import com.music.common.music.domain.enums.PowerTypeEnum;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.request.AddSongToPlaylistReq;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import com.music.common.music.service.IPlaylistService;
import com.music.common.music.service.IPlaylistSongService;
import com.music.common.music.service.adapter.PlaylistAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlaylistSongServiceImpl implements IPlaylistSongService {
    @Autowired
    private PlaylistSongDao playlistSongDao;


    @Override
    public void addSongsToPlaylist(Long playlistId, List<Long> songIds) {
        // 获取当前歌单已有的歌曲 ID
        Set<Long> existingSongIds = playlistSongDao.getExistingSongIds(playlistId);

        // 过滤出需要新增的歌曲
        List<PlaylistSong> playlistSongs = songIds.stream()
                .filter(songId -> !existingSongIds.contains(songId))
                .map(songId -> PlaylistSong.builder()
                        .playlistId(playlistId)
                        .songId(songId)
                        .build())
                .collect(Collectors.toList());

        // 批量保存
        playlistSongDao.savePlaylistSongs(playlistSongs);
    }
}
