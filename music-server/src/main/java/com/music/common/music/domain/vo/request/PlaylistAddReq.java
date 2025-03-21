package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新建歌单请求体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistAddReq {

    @NotBlank
    @Length(max = 6)
    @ApiModelProperty("歌单名")
    private String name;

    @NotBlank
    @ApiModelProperty("封面url")
    private String cover;

    @NotNull
    @ApiModelProperty("是否公开")
    private Integer isPublic;
}
