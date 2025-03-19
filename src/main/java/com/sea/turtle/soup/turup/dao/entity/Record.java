package com.sea.turtle.soup.turup.dao.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Record {

    private Integer id; // 记录ID
    private Integer userId; // 用户ID，关联 user.id
    private Integer puzzleId; // 题目ID，关联 puzzle.id
    private Integer score; // 得分（0-10）
    private Integer questionCount; // 提问次数
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime; // 结束时间
}
