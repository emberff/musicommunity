package com.music.common.music.service.impl;

import com.music.common.common.utils.AssertUtil;
import com.music.common.common.utils.RequestHolder;
import com.music.common.music.dao.*;
import com.music.common.music.domain.entity.*;
import com.music.common.music.domain.enums.PowerTypeEnum;
import com.music.common.music.domain.vo.reponse.SimpleSongListResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.domain.vo.request.PlaylistAddReq;
import com.music.common.music.domain.vo.request.PlaylistUpdateReq;
import com.music.common.music.service.IPlaylistService;
import com.music.common.music.service.ISongService;
import com.music.common.music.service.adapter.PlaylistAdapter;
import com.music.common.music.service.adapter.SongAdapter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SongServiceImpl implements ISongService {
    @Autowired
    private SongDao songDao;
    @Autowired
    private PlaylistDao playlistDao;
    @Autowired
    private SingerDao singerDao;


    @Override
    public SongDetailResp getSongDetail(Long id) {
        Song song = songDao.getById(id);
        AssertUtil.isNotEmpty(song, "未查询到歌曲!");
        // 专辑信息
        Playlist playlist = playlistDao.getById(song.getPlaylistId());
        // 歌手信息
        Singer singer = singerDao.getById(song.getSingerId());
        return SongAdapter.buildSongDetail(song, playlist, singer);
    }


}
