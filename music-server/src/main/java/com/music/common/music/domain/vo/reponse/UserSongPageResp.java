package com.music.common.music.domain.vo.reponse;

import com.music.common.music.domain.entity.Song;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSongPageResp extends Song {

    @ApiModelProperty(value = "上传用户")
    private String userName;

    @ApiModelProperty(value = "评论数")
    private Integer commentNum;


}
