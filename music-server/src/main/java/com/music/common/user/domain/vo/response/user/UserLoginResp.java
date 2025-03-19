package com.music.common.user.domain.vo.response.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录信息")
public class UserLoginResp {
    @ApiModelProperty(value = "token")
    private String token;
}
