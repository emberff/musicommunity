package com.music.common.music.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: ws前端请求类型枚举
 * Author: <a href="https://github.com/emberff">emberff</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum PowerTypeEnum {
    CREATOR(0, "创建者"),
    ADMIN(1, "管理员"),
    BLACK(2, "黑名单")
    ;

    private final Integer value;
    private final String desc;

    private static Map<Integer, PowerTypeEnum> cache;

    static {
        cache = Arrays.stream(PowerTypeEnum.values()).collect(Collectors.toMap(PowerTypeEnum::getValue, Function.identity()));
    }

    public static PowerTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
