package com.sea.turtle.soup.turup.dao.entity;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String openid;
    private String nickname;
    private String avatar;
    private LocalDateTime createTime;
}
