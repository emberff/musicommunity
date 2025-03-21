package com.music.common.music.dao;

import com.music.common.music.domain.entity.Song;
import com.music.common.music.mapper.SongMapper;
import com.music.common.music.service.ISongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌曲表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@Service
public class SongDao extends ServiceImpl<SongMapper, Song>{

}
