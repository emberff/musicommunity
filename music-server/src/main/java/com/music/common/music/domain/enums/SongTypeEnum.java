package com.music.common.music.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SongTypeEnum {
    UPLOAD(0, "专用户上传"),
    JAMENDO_API(1, "Jamendo api获取"),
    ;

    private final Integer value;
    private final String desc;
}
