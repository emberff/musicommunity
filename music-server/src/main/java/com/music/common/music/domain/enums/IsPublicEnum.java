package com.music.common.music.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IsPublicEnum {
    NOT_PUBLIC(0, "否"),
    IS_PUBLIC(1, "是");

    private final Integer value;
    private final String desc;
}
