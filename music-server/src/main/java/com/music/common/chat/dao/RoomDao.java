package com.music.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.music.common.chat.domain.entity.Room;
import com.music.common.chat.mapper.RoomMapper;
import com.music.common.user.domain.entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 房间表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2023-07-16
 */
@Service
public class RoomDao extends ServiceImpl<RoomMapper, Room> implements IService<Room> {

    public void refreshActiveTime(Long roomId, Long msgId, Date msgTime) {
        lambdaUpdate()
                .eq(Room::getId, roomId)
                .set(Room::getLastMsgId, msgId)
                .set(Room::getActiveTime, msgTime)
                .update();
    }

    public Map<Long, Room> getBatch(List<Long> roomIds) {
        List<Room> roomList = lambdaQuery()
                .in(Room::getId, roomIds)
                .list();
        return roomList.stream().collect(Collectors.toMap(Room::getId, Function.identity()));
    }
}
