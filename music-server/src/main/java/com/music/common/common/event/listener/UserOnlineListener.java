package com.music.common.common.event.listener;

import com.music.common.common.event.UserOnlineEvent;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.service.FriendService;
import com.music.common.user.service.cache.UserCache;
import com.music.common.websocket.domain.enums.ChatActiveStatusEnum;
import com.music.common.websocket.service.WebSocketService;
import com.music.common.websocket.service.adapter.WSAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户上线监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class UserOnlineListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCache userCache;
    @Autowired
    private WSAdapter wsAdapter;
    @Autowired
    private FriendService friendService;
//    @Autowired
//    private IpService ipService;
//    @Autowired
//    private PushService pushService;

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        User user = event.getUser();
        userCache.online(user.getId(), user.getLastOptTime());
        //推送给所有在线用户，该用户登录成功 => 暂改为推送给好友
//        webSocketService.sendToAllOnline(wsAdapter.buildOnlineNotifyResp(event.getUser()));
        List<Long> uids = friendService.friendUids(user.getId());
        uids.forEach(uid -> {
            webSocketService.sendToUid(wsAdapter.buildOnlineNotifyResp(event.getUser()), uid);
        });
//        pushService.sendPushMsg(wsAdapter.buildOnlineNotifyResp(event.getUser()));
    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(ChatActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        //更新用户ip详情
//        ipService.refreshIpDetailAsync(user.getId());
    }

}
