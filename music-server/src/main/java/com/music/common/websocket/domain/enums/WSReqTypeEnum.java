package com.music.common.websocket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: ws前端请求类型枚举
 * Author: <a href="https://github.com/emberff">pf</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum WSReqTypeEnum {
    LOGIN(1, "请求连接"),
    HEARTBEAT(2, "心跳包"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, WSReqTypeEnum> cache;

    static {
        cache = Arrays.stream(WSReqTypeEnum.values()).collect(Collectors.toMap(WSReqTypeEnum::getType, Function.identity()));
    }

    public static WSReqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
