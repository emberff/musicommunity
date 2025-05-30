package com.music.common.chat.service.impl;

import com.music.common.chat.dao.GroupMemberDao;
import com.music.common.chat.dao.RoomDao;
import com.music.common.chat.dao.RoomFriendDao;
import com.music.common.chat.dao.RoomGroupDao;
import com.music.common.chat.domain.entity.GroupMember;
import com.music.common.chat.domain.entity.Room;
import com.music.common.chat.domain.entity.RoomFriend;
import com.music.common.chat.domain.entity.RoomGroup;
import com.music.common.chat.domain.enums.GroupRoleEnum;
import com.music.common.chat.domain.enums.RoomTypeEnum;
import com.music.common.chat.service.RoomService;
import com.music.common.chat.service.adapter.ChatAdapter;
import com.music.common.common.domain.enums.NormalOrNoEnum;
import com.music.common.common.utils.AssertUtil;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-07-22
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private GroupMemberDao groupMemberDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomGroupDao roomGroupDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomFriend createFriendRoom(List<Long> uidList) {
        AssertUtil.isNotEmpty(uidList, "房间创建失败，好友数量不对");
        AssertUtil.equal(uidList.size(), 2, "房间创建失败，好友数量不对");
        String key = ChatAdapter.generateRoomKey(uidList);

        RoomFriend roomFriend = roomFriendDao.getByKey(key);
        if (Objects.nonNull(roomFriend)) { //如果存在房间就恢复，适用于恢复好友场景
            restoreRoomIfNeed(roomFriend);
        } else {//新建房间
            Room room = createRoom(RoomTypeEnum.FRIEND);
            roomFriend = createFriendRoom(room.getId(), uidList);
        }
        return roomFriend;
    }

    @Override
    public RoomFriend getFriendRoom(Long uid1, Long uid2) {
        String key = ChatAdapter.generateRoomKey(Arrays.asList(uid1, uid2));
        return roomFriendDao.getByKey(key);
    }

    @Override
    public void disableFriendRoom(List<Long> uidList) {
        AssertUtil.isNotEmpty(uidList, "房间创建失败，好友数量不对");
        AssertUtil.equal(uidList.size(), 2, "房间创建失败，好友数量不对");
        String key = ChatAdapter.generateRoomKey(uidList);
        roomFriendDao.disableRoom(key);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomGroup createGroupRoom(Long uid, String name, String avatar) {
        List<GroupMember> selfGroup = groupMemberDao.getSelfGroup(uid);
//        AssertUtil.isEmpty(selfGroup, "每个人只能创建一个群");
        User user = userDao.getById(uid);
        Room room = createRoom(RoomTypeEnum.GROUP);
        //插入群
        RoomGroup roomGroup = ChatAdapter.buildGroupRoom2(room.getId(), name, avatar);
        roomGroupDao.save(roomGroup);
        //插入群主
        GroupMember leader = GroupMember.builder()
                .role(GroupRoleEnum.LEADER.getType())
                .groupId(roomGroup.getId())
                .uid(uid)
                .build();
        groupMemberDao.save(leader);
        return roomGroup;
    }

    private RoomFriend createFriendRoom(Long roomId, List<Long> uidList) {
        RoomFriend insert = ChatAdapter.buildFriendRoom(roomId, uidList);
        roomFriendDao.save(insert);
        return insert;
    }

    private Room createRoom(RoomTypeEnum typeEnum) {
        Room insert = ChatAdapter.buildRoom(typeEnum);
        roomDao.save(insert);
        return insert;
    }

    private void restoreRoomIfNeed(RoomFriend room) {
        if (Objects.equals(room.getStatus(), NormalOrNoEnum.NOT_NORMAL.getStatus())) {
            roomFriendDao.restoreRoom(room.getId());
        }
    }
}
