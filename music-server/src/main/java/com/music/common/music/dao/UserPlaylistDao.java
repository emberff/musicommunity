package com.music.common.music.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.entity.UserPlaylist;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.music.common.music.mapper.UserPlaylistMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户歌单关联表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@Service
public class UserPlaylistDao extends ServiceImpl<UserPlaylistMapper, UserPlaylist>{

    public IPage<UserPlaylist> getPage(Long uid, Page page) {
        return lambdaQuery()
                .eq(UserPlaylist::getUserId, uid)
                .page(page);
    }

    public IPage<UserPlaylist> getPage(Page page) {
        return lambdaQuery()
                .page(page);
    }
}
