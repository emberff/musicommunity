package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 歌曲详情返回类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("歌曲详情")
public class SongDetailResp {

    @ApiModelProperty(value = "歌曲id")
    private Long songId;

    @ApiModelProperty(value = "歌曲名")
    private String songName;

    @ApiModelProperty(value = "封面url")
    private String cover;

    @ApiModelProperty(value = "歌曲url")
    private String url;

    @ApiModelProperty(value = "歌手id")
    private Long singerId;

    @ApiModelProperty(value = "歌手名")
    private String singerName;

    @ApiModelProperty(value = "专辑id")
    private Long playlistId;

    @ApiModelProperty(value = "专辑名")
    private String playlistName;


}
