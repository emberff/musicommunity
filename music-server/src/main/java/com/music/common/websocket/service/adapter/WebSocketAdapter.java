package com.music.common.websocket.service.adapter;

import com.music.common.user.domain.entity.User;
import com.music.common.websocket.domain.enums.WSRespTypeEnum;
import com.music.common.websocket.domain.vo.resp.WSBaseResp;
import com.music.common.websocket.domain.vo.resp.WSLogin;
import com.music.common.websocket.domain.vo.resp.WSLoginSuccess;

public class WebSocketAdapter {

    public static WSBaseResp<?> buildResp(User user, String token) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess build = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .build();
        resp.setData(build);
        return resp;
    }

    public static WSBaseResp<?> buildInvalidAuthorizeResp() {
        WSBaseResp<WSLogin> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return resp;
    }
}
