package com.music.common.music.service.impl;

import com.music.common.common.utils.AssertUtil;
import com.music.common.music.dao.PlaylistDao;
import com.music.common.music.dao.SingerDao;
import com.music.common.music.dao.SongDao;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.Singer;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.vo.reponse.SingerDetailResp;
import com.music.common.music.domain.vo.reponse.SongDetailResp;
import com.music.common.music.service.ISingerService;
import com.music.common.music.service.ISongService;
import com.music.common.music.service.adapter.SingerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SingerServiceImpl implements ISingerService {
    @Autowired
    private SingerDao singerDao;

    @Override
    public SingerDetailResp getSingerDetail(Long id) {
        Singer singer = singerDao.getById(id);
        AssertUtil.isNotEmpty(singer, "未查询到歌手信息!");
        return SingerAdapter.buildSingerDetail(singer);
    }
}
