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

    public static PlaylistDetailResp buildPlaylistDetail(Playlist playlist, List<PlaylistSong> simpleSongList) {
        PlaylistDetailResp playlistDetailResp = new PlaylistDetailResp();
        BeanUtil.copyProperties(playlist, playlistDetailResp);
        List<SimpleSongListResp> simpleSongListResps = BeanUtil.copyToList(simpleSongList, SimpleSongListResp.class);
        playlistDetailResp.setSongs(simpleSongListResps);
        return playlistDetailResp;
    }
}
