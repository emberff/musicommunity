package com.music.common.common.event.listener;

import com.music.common.chat.dao.*;
import com.music.common.chat.domain.entity.Message;
import com.music.common.chat.domain.entity.Room;
import com.music.common.chat.domain.entity.RoomFriend;
import com.music.common.chat.domain.entity.RoomGroup;
import com.music.common.chat.domain.enums.HotFlagEnum;
import com.music.common.chat.domain.enums.RoomTypeEnum;
import com.music.common.chat.domain.vo.response.ChatMessageResp;
import com.music.common.chat.service.ChatService;
import com.music.common.common.event.MessageSendEvent;
import com.music.common.user.service.cache.UserCache;
import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import com.music.common.websocket.service.WebSocketService;
import com.music.common.websocket.service.adapter.WSAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 消息发送监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class MessageSendListener {
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private GroupMemberDao groupMemberDao;
    @Autowired
    private RoomGroupDao roomGroupDao;
    @Autowired
    private WebSocketService webSocketService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        Message message = messageDao.getById(msgId);
        Room room = roomDao.getById(message.getRoomId());
        ChatMessageResp msgResp = chatService.getMsgResp(message, null);

        // 所有房间更新房间最新消息
        roomDao.refreshActiveTime(room.getId(), message.getId(), message.getCreateTime());

        if (room.isHotRoom()) { // 热门群聊推送所有在线的人
            roomDao.refreshActiveTime(room.getId(), message.getId(), message.getCreateTime());
            // 直接调用 WebSocket 推送给所有在线用户
            webSocketService.sendToAllOnline(WSAdapter.buildMsgSend(msgResp), null);
        } else {
            List<Long> memberUidList = new ArrayList<>();
            if (Objects.equals(room.getType(), RoomTypeEnum.GROUP.getType())) { // 普通群聊
                RoomGroup roomGroup = roomGroupDao.getByRoomId(message.getRoomId());
                memberUidList = groupMemberDao.getMemberUidList(roomGroup.getId());
            } else if (Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())) { // 单聊
                RoomFriend roomFriend = roomFriendDao.getByRoomId(room.getId());
                memberUidList = Arrays.asList(roomFriend.getUid1(), roomFriend.getUid2());
            }

            // 更新所有群成员的会话时间
            contactDao.refreshOrCreateActiveTime(room.getId(), memberUidList, message.getId(), message.getCreateTime());

            // 直接推送给房间成员
            memberUidList.forEach(uid -> {
                webSocketService.sendToUid(WSAdapter.buildMsgSend(msgResp), uid);
            });
        }
    }


//    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
//    public void handlerMsg(@NotNull MessageSendEvent event) {
//        Message message = messageDao.getById(event.getMsgId());
//        Room room = roomCache.get(message.getRoomId());
//        if (isHotRoom(room)) {
//            openAIService.chat(message);
//        }
//    }

    public boolean isHotRoom(Room room) {
        return Objects.equals(HotFlagEnum.YES.getType(), room.getHotFlag());
    }



}
