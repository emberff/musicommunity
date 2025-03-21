package com.music.common.music.service.adapter;

import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.Singer;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SongAdapter {

    public static SongDetailResp buildSongDetail(Song song, Playlist playlist, Singer singer) {
        SongDetailResp songDetailResp = new SongDetailResp();
        songDetailResp.setSongId(song.getId());
        songDetailResp.setSongName(song.getName());
        songDetailResp.setCover(song.getCover());
        songDetailResp.setUrl(song.getUrl());
        songDetailResp.setPlaylistId(playlist.getId());
        songDetailResp.setPlaylistName(playlist.getName());
        songDetailResp.setSingerId(singer.getId());
        songDetailResp.setSingerName(singer.getName());
        return songDetailResp;
    }
}
