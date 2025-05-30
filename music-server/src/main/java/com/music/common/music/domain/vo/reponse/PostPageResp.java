package com.music.common.music.domain.vo.reponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发帖分页返回体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发帖分页返回体")
public class PostPageResp {

    @ApiModelProperty("帖子id")
    private Long id;

    @ApiModelProperty("帖子描述")
    private String description;

    @ApiModelProperty("点赞数")
    private Integer likeCnt;

    @ApiModelProperty("评论数")
    private Integer commentCnt;

    @ApiModelProperty("url")
    private String url;

}
