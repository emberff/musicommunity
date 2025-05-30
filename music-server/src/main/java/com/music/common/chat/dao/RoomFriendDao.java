package com.music.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.music.common.chat.domain.entity.Room;
import com.music.common.chat.domain.entity.RoomFriend;
import com.music.common.chat.mapper.RoomFriendMapper;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 单聊房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2023-07-22
 */
@Service
public class RoomFriendDao extends ServiceImpl<RoomFriendMapper, RoomFriend> {

    public RoomFriend getByKey(String key) {
        return lambdaQuery().eq(RoomFriend::getRoomKey, key).one();
    }

    public void restoreRoom(Long id) {
        lambdaUpdate()
                .eq(RoomFriend::getId, id)
                .set(RoomFriend::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .update();
    }

    public void disableRoom(String key) {
        lambdaUpdate()
                .eq(RoomFriend::getRoomKey, key)
                .set(RoomFriend::getStatus, NormalOrNoEnum.NOT_NORMAL.getStatus())
                .update();
    }

    public List<RoomFriend> listByRoomIds(List<Long> roomIds) {
        return lambdaQuery()
                .in(RoomFriend::getRoomId, roomIds)
                .list();
    }

    public RoomFriend getByRoomId(Long roomID) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomId, roomID)
                .one();
    }

    public Map<Long, RoomFriend> getBatch(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds)) {
            return Collections.emptyMap();
        }
        List<RoomFriend> roomFriendList = lambdaQuery()
                .in(RoomFriend::getRoomId, roomIds)
                .list();
        return roomFriendList.stream()
                .collect(Collectors.toMap(RoomFriend::getRoomId, Function.identity(), (a, b) -> b));
    }

}
