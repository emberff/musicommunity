package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 歌单详细信息请求体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("歌单详情")
public class PlaylistDetailResp {
    @ApiModelProperty(value = "歌单id")
    private Long id;

    @ApiModelProperty(value = "歌单名")
    private String name;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "是否公开 0否 1是")
    private Integer isPublic;

    @ApiModelProperty(value = "歌单歌曲数")
    private Integer plSongNum;

    @ApiModelProperty(value = "歌单播放数")
    private Integer plListenNum;

    @ApiModelProperty(value = "歌单分享数")
    private Integer plShareNum;

    @ApiModelProperty(value = "歌单评论数")
    private Integer plCommentNum;

    @ApiModelProperty(value = "歌单收藏数")
    private Integer plFollowNum;

    @ApiModelProperty(value = "是否收藏")
    private Integer isFollowed;

    @ApiModelProperty(value = "创建者id")
    private Long creatorId;

    @ApiModelProperty(value = "歌曲列表")
    private List<SimpleSongListResp> songs;

}
