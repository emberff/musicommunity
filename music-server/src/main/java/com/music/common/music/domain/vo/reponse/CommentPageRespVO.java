package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "评论分页响应 VO")
public class CommentPageRespVO {

    @ApiModelProperty(value = "评论 ID")
    private Long id;

    @ApiModelProperty(value = "用户 ID")
    private Long userId;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "歌曲 ID")
    private Long songId;

    @ApiModelProperty(value = "父评论 ID，根节点为 0")
    private Long parentId;

    @ApiModelProperty(value = "评论内容")
    private String cotent;

    @ApiModelProperty(value = "是否禁用（0 否，1 是）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "子评论列表")
    private List<CommentPageRespVO> children;
}
