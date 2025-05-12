package com.music.common.common.event.listener;

import com.music.common.chat.domain.dto.ChatMsgRecallDTO;
import com.music.common.chat.service.ChatService;
import com.music.common.common.event.MessageRecallEvent;
import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import com.music.common.websocket.service.WebSocketService;
import com.music.common.websocket.service.adapter.WSAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 消息撤回监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class MessageRecallListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;

//    @Async
//    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
//    public void evictMsg(MessageRecallEvent event) {
//        ChatMsgRecallDTO recallDTO = event.getRecallDTO();
//        msgCache.evictMsg(recallDTO.getMsgId());
//    }

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void sendToAll(MessageRecallEvent event) {
        //针对room, 撤回直接通知room内所有用户
        WSBaseResp<?> wsBaseResp = WSAdapter.buildMsgRecall(event.getRecallDTO());
        webSocketService.sendToAllOnline(wsBaseResp, null);
    }

}
