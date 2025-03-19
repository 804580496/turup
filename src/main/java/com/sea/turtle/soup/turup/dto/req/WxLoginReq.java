package com.sea.turtle.soup.turup.dto.req;

import lombok.Data;

@Data
public class WxLoginReq {


    private String code;

    private String nickname;
    private String avatar;
}
