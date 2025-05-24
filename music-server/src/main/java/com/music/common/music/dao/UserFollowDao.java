package com.music.common.music.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.music.common.music.domain.entity.UserFollow;
import com.music.common.music.mapper.UserFollowMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户关注歌手表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-05-24
 */
@Service
public class UserFollowDao extends ServiceImpl<UserFollowMapper, UserFollow> implements IService<UserFollow> {

    public UserFollow getBySingerIdAndUid(Long singerId, Long uid) {
        return lambdaQuery().eq(UserFollow::getSingerId, singerId)
                .eq(UserFollow::getUserId, uid)
                .one();
    }
}
