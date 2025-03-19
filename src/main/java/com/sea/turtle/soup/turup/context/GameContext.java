package com.sea.turtle.soup.turup.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameContext {
    private Integer userId; // 用户ID
    private Integer gameId; // 游戏ID
}
