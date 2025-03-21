package com.music.common.music.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.entity.PlaylistSong;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.mapper.PlaylistSongMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 歌单歌曲关联表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@Service
public class PlaylistSongDao extends ServiceImpl<PlaylistSongMapper, PlaylistSong> implements IService<PlaylistSong> {

    public List<PlaylistSong> getSimpleSongListByPlaylistId(Long playlistId) {
        return lambdaQuery().eq(PlaylistSong::getPlaylistId, playlistId).list();
    }

    public Set<Long> getExistingSongIds(Long playlistId) {
        return lambdaQuery()
                .select(PlaylistSong::getSongId)
                .eq(PlaylistSong::getPlaylistId, playlistId)
                .list()
                .stream()
                .map(PlaylistSong::getSongId)
                .collect(Collectors.toSet());
    }

    public void savePlaylistSongs(List<PlaylistSong> playlistSongs) {
        if (!playlistSongs.isEmpty()) {
            this.saveBatch(playlistSongs);
        }
    }
}
