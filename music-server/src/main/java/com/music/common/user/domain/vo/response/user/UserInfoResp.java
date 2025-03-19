package com.music.common.user.domain.vo.response.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户详情")
public class UserInfoResp {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户昵称")
    private String name;

    @ApiModelProperty(value = "性别 1为男性，2为女性")
    private Integer sex;


}
