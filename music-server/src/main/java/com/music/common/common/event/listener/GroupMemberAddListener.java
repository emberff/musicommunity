package com.music.common.common.event.listener;

import com.music.common.chat.dao.GroupMemberDao;
import com.music.common.chat.domain.entity.GroupMember;
import com.music.common.chat.domain.entity.RoomGroup;
import com.music.common.chat.domain.vo.request.ChatMessageReq;
import com.music.common.chat.service.ChatService;
import com.music.common.chat.service.adapter.MemberAdapter;
import com.music.common.chat.service.adapter.RoomAdapter;
import com.music.common.common.event.GroupMemberAddEvent;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import com.music.common.websocket.domain.vo.resp.WSMemberChange;
import com.music.common.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 添加群成员监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class GroupMemberAddListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private GroupMemberDao groupMemberDao;
    @Autowired
    private UserDao userDao;

    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendAddMsg(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        Long inviteUid = event.getInviteUid();
        User user = userDao.getById(inviteUid);
        List<Long> uidList = memberList.stream().map(GroupMember::getUid).collect(Collectors.toList());
        ChatMessageReq chatMessageReq = RoomAdapter.buildGroupAddMessage(roomGroup, user, userDao.getBatch(uidList));
//        chatService.sendMsg(chatMessageReq, User.UID_SYSTEM);
        chatService.sendMsg(chatMessageReq, inviteUid);
    }

    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendChangePush(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        List<Long> memberUidList = groupMemberDao.getMemberUidList(roomGroup.getRoomId());
        List<Long> uidList = memberList.stream().map(GroupMember::getUid).collect(Collectors.toList());
        List<User> users = userDao.listByIds(uidList);
        users.forEach(user -> {
            WSBaseResp<WSMemberChange> ws = MemberAdapter.buildMemberAddWS(roomGroup.getRoomId(), user);
            memberUidList.forEach(uid -> {
                webSocketService.sendToUid(ws, uid);
            });
        });
        //移除缓存
//        groupMemberCache.evictMemberUidList(roomGroup.getRoomId());
    }

}
