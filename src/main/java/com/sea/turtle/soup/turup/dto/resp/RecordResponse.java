package com.sea.turtle.soup.turup.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordResponse {

    private int recordId;
    private int score;
    private int questionCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int puzzleId;
    private String puzzleTitle;
    private String puzzleType;
    private String puzzleDifficulty;
}
