package com.music.common.common.event.listener;

import com.music.common.common.event.UserApplyEvent;
import com.music.common.user.dao.UserApplyDao;
import com.music.common.user.domain.entity.UserApply;
import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import com.music.common.websocket.domain.vo.resp.WSFriendApply;
import com.music.common.websocket.service.WebSocketService;
import com.music.common.websocket.service.adapter.WSAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 好友申请监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class UserApplyListener {
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    private WebSocketService webSocketService;

    //TODO: RocketMQ, 暂不接入.后续重构或修改时再启用.现在先用订阅发布模式简单处理消息推送
//    @Autowired
//    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = UserApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(UserApplyEvent event) {
        UserApply userApply = event.getUserApply();
        Integer unReadCount = userApplyDao.getUnReadCount(userApply.getTargetId());
        WSBaseResp<WSFriendApply> wsFriendApplyWSBaseResp = WSAdapter.buildApplySend(new WSFriendApply(userApply.getUid(), unReadCount));
        webSocketService.sendToUid(wsFriendApplyWSBaseResp, userApply.getTargetId());
//        pushService.sendPushMsg(WSAdapter.buildApplySend(new WSFriendApply(userApply.getUid(), unReadCount)), userApply.getTargetId());
    }

}
