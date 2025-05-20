package com.music.common.music.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.music.common.music.domain.entity.PlaylistRec;
import com.music.common.music.domain.entity.SongRec;
import com.music.common.music.mapper.PlaylistRecMapper;
import com.music.common.music.mapper.SongRecMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistRecDao extends ServiceImpl<PlaylistRecMapper, PlaylistRec> {
    public List<Long> getByUid(Long uid) {
        return lambdaQuery()
                .eq(PlaylistRec::getUserId, uid)
                .list()
                .stream()
                .map(PlaylistRec::getRecPlaylistId)
                .collect(Collectors.toList());
    }


}
