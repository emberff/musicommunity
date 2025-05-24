package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 歌单分页信息请求体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("歌手分页")
public class SingerPageResp {
    @ApiModelProperty(value = "歌手id")
    private Long id;

    @ApiModelProperty(value = "歌手名")
    private String name;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "是否关注")
    private Integer isFollowed;
}
