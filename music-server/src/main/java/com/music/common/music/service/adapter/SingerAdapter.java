package com.music.common.music.service.adapter;

import com.music.common.music.domain.entity.Singer;
import com.music.common.music.domain.vo.reponse.SingerDetailResp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingerAdapter {
    public static SingerDetailResp buildSingerDetail(Singer singer) {
        SingerDetailResp singerDetailResp = new SingerDetailResp();
        singerDetailResp.setSingerId(singer.getId());
        singerDetailResp.setSingerName(singer.getName());
        singerDetailResp.setAlbumNum(singer.getAlbumNum());
        singerDetailResp.setFollowNum(singer.getFollowNum());
        return singerDetailResp;
    }

    // TODO 歌曲详细信息
//    public static SongDetailResp buildSongDetail(Song song, Playlist playlist, Singer singer) {
//        Playlist playlist = new Playlist();
//        playlist.setId(updateReq.getId());
//        playlist.setName(updateReq.getName());
//        playlist.setCover(updateReq.getCover());
//        playlist.setIsPublic(updateReq.getIsPublic());
//        return playlist;
//    }
}
