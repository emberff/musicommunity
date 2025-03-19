package com.music.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserTypeEnum {
    Admin(0, "管理员"),
    User(1, "用户");

    private final Integer value;
    private final String desc;
}
