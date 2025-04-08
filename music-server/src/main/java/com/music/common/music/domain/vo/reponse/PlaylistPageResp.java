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
@ApiModel("歌单分页")
public class PlaylistPageResp {
    @ApiModelProperty(value = "歌单id")
    private Long id;

    @ApiModelProperty(value = "歌单名")
    private String name;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "歌单歌曲数")
    private Integer plSongNum;
}
