package com.music.common.music.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.PlaylistSong;
import com.music.common.music.domain.vo.reponse.PlaylistDetailResp;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class PlaylistAdapter {

    public static Playlist buildPlaylist(PlaylistUpdateReq updateReq) {
        Playlist playlist = new Playlist();
        BeanUtil.copyProperties(updateReq, playlist);
        return playlist;
    }

    public static PlaylistDetailResp buildPlaylistDetail(Playlist playlist, List<SimpleSongListResp> simpleSongList) {
        PlaylistDetailResp playlistDetailResp = new PlaylistDetailResp();
        BeanUtil.copyProperties(playlist, playlistDetailResp);
        playlistDetailResp.setSongs(simpleSongList);
        return playlistDetailResp;
    }

    public static PlaylistDetailResp buildPlaylistDetail(Playlist playlist, List<SimpleSongListResp> simpleSongList, Integer isFollowed) {
        PlaylistDetailResp playlistDetailResp = new PlaylistDetailResp();
        BeanUtil.copyProperties(playlist, playlistDetailResp);
        playlistDetailResp.setSongs(simpleSongList);
        playlistDetailResp.setIsFollowed(isFollowed);
        playlistDetailResp.setCreatorId(playlist.getPlCreatorId());
        return playlistDetailResp;
    }
}
