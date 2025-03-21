package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简易歌曲信息列表类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("歌曲列表返回体")
public class SimpleSongListResp {

    @ApiModelProperty("歌曲id")
    private Long id;

    @ApiModelProperty("歌曲名")
    private String name;

    @ApiModelProperty("歌曲封面")
    private String cover;
}
