package com.music.common.music.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.vo.resp.PageBaseResp;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.Singer;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

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

    public static PageBaseResp<SimpleSongListResp> buildSimpleSongListRespPage(List<Song> songs, IPage page) {
        List<SimpleSongListResp> lists = new ArrayList<>();
        for (Song song : songs) {
            SimpleSongListResp simpleSongListResp = new SimpleSongListResp();
            BeanUtil.copyProperties(song, simpleSongListResp);
            lists.add(simpleSongListResp);
        }
        return PageBaseResp.init(page, lists); // 正确调用，page.getTotal() 才有值
    }

}
