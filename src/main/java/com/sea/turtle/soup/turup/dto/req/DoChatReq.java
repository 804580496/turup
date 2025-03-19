package com.sea.turtle.soup.turup.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoChatReq {

    private String messages;
    private String soup;
    private String soupNoodles;
}
