package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 歌手信息详情返回类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("歌曲详情")
public class SingerDetailResp {

    @ApiModelProperty(value = "歌手id")
    private Long singerId;

    @ApiModelProperty(value = "歌手名")
    private String singerName;

    @ApiModelProperty(value = "专辑数")
    private Integer albumNum;

    @ApiModelProperty(value = "关注数")
    private Integer followNum;

//    @ApiModelProperty(value = "歌曲列表")

}
