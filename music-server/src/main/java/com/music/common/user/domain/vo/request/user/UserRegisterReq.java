package com.music.common.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterReq {

    @ApiModelProperty(value = "手机号", required = true)
    @NotNull
    private String phone;

    @ApiModelProperty(value = "用户昵称", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(value = "头像",required = true)
    private String avatar;

    @ApiModelProperty(value = "性别 1为男性，2为女性", required = true)
    private Integer sex;
}
