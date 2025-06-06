package com.music.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.music.common.websocket.domain.enums.WSReqTypeEnum;
import com.music.common.websocket.domain.vo.req.WSBaseReq;
import com.music.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connnect(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffline(ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        super.exceptionCaught(ctx, cause);
    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        //握手完成
//        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
//            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
//            if (StrUtil.isNotBlank(token)) {
//                webSocketService.authorize(ctx.channel(), token);
//
//            }
//        //读空闲
//        } else if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent)evt;
//            if (event.state() == IdleState.READER_IDLE) {
//                System.out.println("读空闲");
//                userOffline(ctx.channel());
//            }
//        }
//    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //握手完成
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if (StrUtil.isNotBlank(token)) {
                webSocketService.authorize(ctx.channel(), token);
            }
        }
    }



    /**
     * 用户下线统一处理
     * @param channel
     */
    private void userOffline(Channel channel) {
        webSocketService.remove(channel);
        channel.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {

            case HEARTBEAT:
                break;
            case LOGIN:
                System.out.println("登录");
                //从前端接收时为TextWebSocketFrame类, 返回时也需要用此类包装, 然后由websocket底层处理再返回
//                ctx.channel().writeAndFlush(new TextWebSocketFrame("谢谢谢谢"));
        }
        System.out.println(text);
    }
}
