package com.music.common.websocket.domain.vo.req;

import com.music.common.websocket.domain.enums.WSReqTypeEnum;
import lombok.Data;

@Data
public class WSBaseReq {
    /**
     * @see WSReqTypeEnum
     */

    public Integer type;
    public String data;
}
