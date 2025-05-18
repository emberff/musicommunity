package com.music.common.music.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.entity.SongRec;
import com.music.common.music.mapper.SongMapper;
import com.music.common.music.mapper.SongRecMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class SongRecDao extends ServiceImpl<SongRecMapper, SongRec> {
    public List<Long> getByUid(Long uid) {
        return lambdaQuery()
                .eq(SongRec::getUserId, uid)
                .list()
                .stream()
                .map(SongRec::getRecSongId)
                .collect(Collectors.toList());
    }


}
