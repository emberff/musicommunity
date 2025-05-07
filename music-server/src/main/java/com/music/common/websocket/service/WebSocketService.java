package com.music.common.websocket.service;


import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import io.netty.channel.Channel;

public interface WebSocketService {

    void connnect(Channel channel);

    void remove(Channel channel);

    void authorize(Channel channel, String token);

    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     * @param skipUid    需要跳过的人
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid);

    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp);

    void sendToUid(WSBaseResp<?> wsBaseResp, Long uid);

}
