package com.sea.turtle.soup.turup.dao.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Puzzle {


    private Integer id; // 题目ID
    private String title; // 题目名称
    private String content; // 汤面内容
    private String answer; // 汤底答案
    private String type; // 题目类型（红汤、清汤、本格、变格）
    private String difficulty; // 难度等级（简单、中等、困难）
    private LocalDateTime createTime; // 创建时间

}
