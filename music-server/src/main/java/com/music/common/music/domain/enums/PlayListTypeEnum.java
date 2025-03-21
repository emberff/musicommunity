package com.music.common.music.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlayListTypeEnum {
    ALBUM(0, "专辑"),
    SYSTEM(1, "系统歌单"),
    USER_FAVOURITE(2, "用户喜欢"),
    USER_PLAYLIST(3, "用户歌单");

    private final Integer value;
    private final String desc;
}
