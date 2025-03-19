package com.sea.turtle.soup.turup.dto.resp;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class WxLoginResponse {
    private String token;        // JWT token
    private Integer userId;      // 用户ID
    private String nickname;     // 用户昵称
    private String avatar;       // 用户头像
    private Long expiresIn;      // token过期时间（秒）
} 