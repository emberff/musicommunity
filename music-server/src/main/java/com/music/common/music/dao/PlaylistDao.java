package com.music.common.music.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.mapper.PlaylistMapper;
import com.music.common.music.service.IPlaylistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌单表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-20
 */
@Service
public class PlaylistDao extends ServiceImpl<PlaylistMapper, Playlist>{

    /**
     * 删除歌单(假删除)
     * @param playlistId
     */
    public void disablePlaylist(Long playlistId) {
        lambdaUpdate()
                .eq(Playlist::getId, playlistId)
                .set(Playlist::getStatus, NormalOrNoEnum.NOT_NORMAL.getStatus())
                .update();
    }

}
