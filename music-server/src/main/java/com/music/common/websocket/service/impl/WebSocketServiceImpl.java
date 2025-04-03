package com.music.common.websocket.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.music.common.user.dao.UserDao;
import com.music.common.user.domain.entity.User;
import com.music.common.user.service.LoginService;
import com.music.common.websocket.NettyUtil;
import com.music.common.websocket.domain.dto.WSChannelExtraDTO;
import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import com.music.common.websocket.service.WebSocketService;
import com.music.common.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 专门管理 WebSocket 的逻辑
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Qualifier("websocketExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 管理所有用户的连接,包括 (用户态/游客)
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    private static final Duration DURATION = Duration.ofHours(1);
    private static final long MAXIMUM_SIZE = 1000;

    /**
     *  临时保存登录 code 和 channel 的映射关系
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();
    @Override
    public void connnect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
    }

    @Override
    public void authorize(Channel channel, String token) {

        Long uid = loginService.getValidUid(token);
        System.out.println(token);
        if (uid != null) {
            // 前端请求token非空, 直接发送token并返回前端
            User user = userDao.getById(uid);
            loginSuccess(channel, user, token);
        } else {
            sendMsg(channel, WebSocketAdapter.buildInvalidAuthorizeResp());
        }
    }

    /**
     * (channel在本地)登录成功，并更新状态
     */
    private void loginSuccess(Channel channel, User user, String token) {
        //更新上线列表
        online(channel, user.getId());
        //返回给用户登录成功
//        boolean hasPower = iRoleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER);
        // 保存channel的对应uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        // 推送成功消息
        sendMsg(channel, WebSocketAdapter.buildResp(user, token));
//        // 用户上线的事件
//        user.setLastOptTime(new Date());
//        user.refreshIp(NettyUtil.getAttr(channel, NettyUtil.IP));
//        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
    }

    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Channel, WSChannelExtraDTO> getOnlineMap() {
        return ONLINE_WS_MAP;
    }

    private void online(Channel channel, Long uid) {
        getOrInitChannelExt(channel).setUid(uid);
        ONLINE_UID_MAP.putIfAbsent(uid, new CopyOnWriteArrayList<>());
        ONLINE_UID_MAP.get(uid).add(channel);
        NettyUtil.setAttr(channel, NettyUtil.UID, uid);
    }

    /**
     * 如果在线列表不存在，就先把该channel放进在线列表
     *
     * @param channel
     * @return
     */
    private WSChannelExtraDTO getOrInitChannelExt(Channel channel) {
        WSChannelExtraDTO wsChannelExtraDTO =
                ONLINE_WS_MAP.getOrDefault(channel, new WSChannelExtraDTO());
        WSChannelExtraDTO old = ONLINE_WS_MAP.putIfAbsent(channel, wsChannelExtraDTO);
        return ObjectUtil.isNull(old) ? wsChannelExtraDTO : old;
    }

//    @Override
//    public void sendMsgToAll(WSBaseResp<?> msg) {
//        ONLINE_WS_MAP.forEach((channel, ext) -> {
//            threadPoolTaskExecutor.execute(() -> {
//                sendMsg(channel, msg);
//            });
//        });
//    }


    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

}
