package com.music.common.music.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.music.domain.entity.Song;
import com.music.common.music.domain.entity.UserPlaylist;
import com.music.common.music.mapper.SongMapper;
import com.music.common.music.service.ISongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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

    public IPage<Song> getPage(Page page) {
        return lambdaQuery()
                .page(page);
    }

    public List<Song> selectBatchIds(List<Long> pagedSongIds) {
        if (pagedSongIds == null || pagedSongIds.isEmpty()) {
            return Collections.emptyList();
        }
        return super.listByIds(pagedSongIds); // 调用 ServiceImpl 的方法
    }
}
