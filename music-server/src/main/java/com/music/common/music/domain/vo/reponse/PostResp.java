package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 发布详情
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResp {

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "简述")
    private String description;

    @ApiModelProperty(value = "文件url")
    private List<String> urls;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "评论数")
    private Integer commentCount;

    @ApiModelProperty(value = "分享数")
    private Integer shareCount;

    @ApiModelProperty(value = "发布时间")
    private Date createTime;
}
