package com.music.common.websocket.service;


import io.netty.channel.Channel;

public interface WebSocketService {

    void connnect(Channel channel);

    void remove(Channel channel);

    void authorize(Channel channel, String token);

}
