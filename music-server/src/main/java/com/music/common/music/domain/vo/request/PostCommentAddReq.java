package com.music.common.music.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentAddReq {


    /**
     * 歌曲id
     */
    @NotNull
    @ApiModelProperty(value = "歌曲id", required = true)
    private Long postId;

    /**
     * 父id
     */
    @ApiModelProperty(value = "回复的评论id", required = false)
    private Long parentId;

    /**
     * 内容
     */
    @NotBlank
    @ApiModelProperty(value = "内容", required = true)
    private String content;
}
