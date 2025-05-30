package com.music.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.music.common.chat.domain.entity.RoomFriend;
import com.music.common.chat.domain.entity.RoomGroup;
import com.music.common.chat.mapper.RoomGroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 群聊房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2023-07-22
 */
@Service
public class RoomGroupDao extends ServiceImpl<RoomGroupMapper, RoomGroup> {

    public List<RoomGroup> listByRoomIds(List<Long> roomIds) {
        return lambdaQuery()
                .in(RoomGroup::getRoomId, roomIds)
                .list();
    }

    public RoomGroup getByRoomId(Long roomId) {
        return lambdaQuery()
                .eq(RoomGroup::getRoomId, roomId)
                .one();
    }

    public Map<Long, RoomGroup> getBatch(List<Long> groupRoomId) {
        if (CollectionUtils.isEmpty(groupRoomId)) {
            return Collections.emptyMap();
        }
        List<RoomGroup> roomGroupList = lambdaQuery()
                .in(RoomGroup::getRoomId, groupRoomId)
                .list();
        return roomGroupList.stream()
                .collect(Collectors.toMap(RoomGroup::getRoomId, Function.identity(), (existing, replacement) -> replacement));
    }

}
