package com.music.common.music.dao;

import com.music.common.music.domain.entity.Power;
import com.music.common.music.mapper.PowerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 歌单/群聊权限表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@Service
public class PowerDao extends ServiceImpl<PowerMapper, Power> {
    /**
     * 校验多个权限类型
     * @param uid
     * @param playlistId
     * @param powerType
     * @return
     */
    public Boolean checkPower(Long uid, Long playlistId, List<Integer> powerType) {
        Power power = lambdaQuery()
                .eq(Power::getUserId, uid)
                .eq(Power::getPlaylistId, playlistId)
                .one();

        return power != null && powerType.contains(power.getPowerType());
    }

    /**
     * 校验单个权限类型
     * @param uid
     * @param playlistId
     * @param powerType
     * @return
     */
    public Boolean checkPower(Long uid, Long playlistId, Integer powerType) {
        Power power = lambdaQuery()
                .eq(Power::getUserId, uid)
                .eq(Power::getPlaylistId, playlistId)
                .one();

        return power != null && powerType.equals(power.getPowerType());
    }


}
