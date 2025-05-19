package com.music.common.user.domain.vo.request.user;

import com.music.common.common.domain.vo.req.PageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchPageReq extends PageBaseReq {

    @NotBlank
    @ApiModelProperty(value = "用户昵称", required = true)
    private String name;
}
