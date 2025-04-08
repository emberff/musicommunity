package com.music.common.music.service.impl;

import com.music.common.music.dao.PlaylistSongDao;
import com.music.common.music.domain.entity.PlaylistSong;
import com.music.common.music.service.IPlaylistSongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<PlaylistSong> songsToAdd = songIds.stream()
                .filter(songId -> !existingSongIds.contains(songId))
                .map(songId -> PlaylistSong.builder()
                        .playlistId(playlistId)
                        .songId(songId)
                        .build())
                .collect(Collectors.toList());

        // 批量保存
        playlistSongDao.savePlaylistSongs(playlistId, songsToAdd);
    }

    @Override
    public void deleteSongToPlaylist(Long playlistId, List<Long> songIds) {
        // 获取当前歌单已有的歌曲 ID
        Set<Long> existingSongIds = playlistSongDao.getExistingSongIds(playlistId);

        // 过滤出需要删除的歌曲（只删除存在于歌单中的歌曲）
        List<Long> songsToDelete = songIds.stream()
                .filter(existingSongIds::contains)
                .collect(Collectors.toList());

        // 批量删除
        if (!songsToDelete.isEmpty()) {
            playlistSongDao.deletePlaylistSongs(playlistId, songsToDelete);
        }
    }

}
