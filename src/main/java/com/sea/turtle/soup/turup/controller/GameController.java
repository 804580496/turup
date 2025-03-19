package com.sea.turtle.soup.turup.controller;

import com.sea.turtle.soup.turup.api.DeepSeekClient;
import com.sea.turtle.soup.turup.context.GameContextHolder;
import com.sea.turtle.soup.turup.dao.entity.Puzzle;
import com.sea.turtle.soup.turup.dao.entity.Record;
import com.sea.turtle.soup.turup.dto.req.DoChatReq;
import com.sea.turtle.soup.turup.dto.req.GameReq;
import com.sea.turtle.soup.turup.dto.resp.DoChartResp;
import com.sea.turtle.soup.turup.dto.resp.GameResp;
import com.sea.turtle.soup.turup.service.PuzzleService;
import com.sea.turtle.soup.turup.service.RecordService;
import com.sea.turtle.soup.turup.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;

@RestController
@Slf4j
@RequestMapping("/api/game")
public class GameController {


    @Autowired
    private DeepSeekClient deepSeekClient;

    @Autowired
    private RecordService recordService;

    @Autowired
    private PuzzleService puzzleService;

    @PostMapping("/start")
    public Result<GameResp> startGame(@RequestBody GameReq gameReq) {
        // 1. 参数校验
        if (gameReq.getDifficulty() == null || gameReq.getKeyNote() == null) {
            return Result.error(404,"难度和基调不能为空");
        }

        // 2. 构建系统提示
        String systemPrompt = buildSystemPrompt(gameReq);
        System.out.println(systemPrompt);
        // 3. 调用DeepSeek生成故事
        String story = deepSeekClient.generateStory(systemPrompt);

        System.out.println(story);
        // 4. 解析返回结果
        GameResp resp = parseStoryResponse(story);

        Puzzle puzzle = Puzzle.builder()
                .answer(resp.getSoup())
                .content(resp.getSoupNuddole())
                .type(resp.getType())
                .difficulty(resp.getDifficulty())
                .title(resp.getStoryTitle())
                .createTime(LocalDateTime.now()).build();
        puzzleService.insert(puzzle);
        Record record = Record.builder()
                .userId(GameContextHolder.getContext().getUserId())
                .puzzleId(puzzle.getId())
                .startTime(LocalDateTime.now()).build();

        recordService.insert(record);
        // 5. 返回结果
        return Result.success(resp);
    }


    @PostMapping("/doChart")
    public Result<DoChartResp> doChart(@RequestBody DoChatReq doChatReq){
        String messages = deepSeekClient.doChat(doChatReq);
        DoChartResp doChartResp = DoChartResp.builder()
                .message(messages).build();
        return Result.success(doChartResp);
    }

    private String buildSystemPrompt(GameReq gameReq) {
        return String.format(
                "你是一个海龟汤谜题生成器，请严格按照以下要求生成谜题：\n" +
                        "1. 难度：%s\n" +
                        "   - 简单：线索明显，适合新手\n" +
                        "   - 普通：需要一定推理能力\n" +
                        "   - 困难：需要深入思考和联想\n" +
                        "2. 基调：%s\n" +
                        "   - 本格：传统推理，注重逻辑\n" +
                        "   - 变格：超现实元素，注重氛围\n" +
                        "3. 类型：%s\n" +
                        "   - 红汤：血腥暴力，适合重口味\n" +
                        "   - 清汤：轻松愉快，适合全年龄\n" +
                        "4. 输出格式（必须严格遵守）：\n" +
                        "   【标题】xxx\n" +
                        "   【难度】xxx\n" +
                        "   【基调】xxx\n" +
                        "   【类型】xxx\n" +
                        "   【汤面】xxx\n" +
                        "   【汤底】xxx\n" +
                        "5. 其他要求：\n" +
                        "   - 汤面：100字以内，留下悬念\n" +
                        "   - 汤底：解释合理但出人意料\n" +
                        "   - 红汤可以包含适当惊悚元素，但避免过度血腥\n" +
                        "   - 清汤应保持轻松愉快，适合全年龄\n" +
                        "   - 确保谜题逻辑自洽",
                gameReq.getDifficulty(),
                gameReq.getKeyNote(),
                gameReq.getType()
        );
    }

    private GameResp parseStoryResponse(String story) {
        GameResp resp = new GameResp();
        try {
            // 解析标题
            resp.setStoryTitle(extractField(story, "【标题】"));

            // 解析难度
            String difficulty = extractField(story, "【难度】");
            if (!List.of("简单", "普通", "困难").contains(difficulty)) {
                throw new IllegalArgumentException("难度值不合法");
            }
            resp.setDifficulty(difficulty);

            // 解析基调
            String keyNote = extractField(story, "【基调】");
            if (!List.of("本格", "变格").contains(keyNote)) {
                throw new IllegalArgumentException("基调值不合法");
            }
            resp.setKeyNote(keyNote);

            // 解析类型
            String type = extractField(story, "【类型】");
            if (!List.of("红汤", "清汤").contains(type)) {
                throw new IllegalArgumentException("类型值不合法");
            }
            resp.setType(type);

            // 解析汤面
            resp.setSoupNuddole(extractField(story, "【汤面】"));

            // 解析汤底
            resp.setSoup(extractField(story, "【汤底】"));





        } catch (Exception e) {


            // 设置默认值
            resp.setStoryTitle("解析失败-神秘的海龟汤");

        }
        return resp;
    }
    private String extractField(String story, String field) {
        int start = story.indexOf(field);
        if (start == -1) return "";

        start += field.length();
        int end = story.indexOf("【", start);
        if (end == -1) end = story.length();

        return story.substring(start, end).trim();
    }
}
