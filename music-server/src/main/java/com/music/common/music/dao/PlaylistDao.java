package com.music.common.music.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.music.domain.entity.Playlist;
import com.music.common.music.domain.entity.PlaylistSong;
import com.music.common.music.domain.entity.Power;
import com.music.common.music.domain.entity.UserPlaylist;
import com.music.common.music.domain.enums.IsPublicEnum;
import com.music.common.music.domain.enums.PlayListTypeEnum;
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

    public Playlist getPlaylistIdbyRoomId(Long roomId) {
        Playlist playlist = lambdaQuery()
                .eq(Playlist::getRoomId, roomId)
                .one();
        return playlist;
    }

    public IPage<Playlist> getPage(Page page) {
        return lambdaQuery()
                .eq(Playlist::getIsPublic, IsPublicEnum.IS_PUBLIC.getValue())
                .ne(Playlist::getType, PlayListTypeEnum.USER_FAVOURITE.getValue())
                .orderByDesc(Playlist::getCreateTime) // 指定按创建时间倒序
                .page(page);
    }
}
