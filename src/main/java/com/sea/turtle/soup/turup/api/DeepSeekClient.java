package com.sea.turtle.soup.turup.api;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sea.turtle.soup.turup.context.GameContextHolder;
import com.sea.turtle.soup.turup.dto.req.DoChatReq;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
@Slf4j
public class DeepSeekClient {
    @Value("${deepseek.api.key}")
    private String apiKey;
    @Value("${deepseek.api.base}")
    private String baseUrl;

    private final Map<Integer,List<String>> conversationHistory = new HashMap<>();

    private final RestTemplate restTemplate;

    public DeepSeekClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateStory(String prompt) {
        try {
            // 创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 创建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "deepseek-chat");
            JSONArray messages = new JSONArray();

            // 添加 user 消息
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", "");
            messages.add(userMessage);
            //添加system信息
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role","system");
            systemMessage.put("content",prompt);
            messages.add(systemMessage);
            requestBody.put("messages", messages);
            requestBody.put("stream",false);
            // 创建 HTTP 请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

            // 发送请求并获取响应
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // 处理响应
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();

                // 解析 JSON 并提取 content
                JSONObject responseJson = JSONUtil.parseObj(responseBody);
                String content = responseJson.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getStr("content");

                return content;
            } else {
                return "Error: " + responseEntity.getStatusCode() + " - " + responseEntity.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating story: " + e.getMessage();
        }
    }
    public String doChat(DoChatReq doChatReq) {
        Integer userId = GameContextHolder.getContext().getUserId();
        List<String> history = conversationHistory.getOrDefault(userId, new ArrayList<>());
        history.add(doChatReq.getMessages());
        conversationHistory.put(userId,history);
        final String systemPrompt = String.format(
                "1. 提供一道海龟汤谜题的「汤面」：%s  \n" +
                        "2. 仅回答“是”、“否”或“与此无关”。  \n" +
                        "3. 在特定情况下结束游戏并揭示“汤底”（故事真相）：%s  \n\n" +
                        "游戏规则：\n" +
                        "1. 用户回答开始先展示汤面\n" +
                        "2. 玩家提问后只能回答,：\n" +
                        "  ○ 是：玩家的猜测与真相相符。\n" +
                        "  ○ 否：玩家的猜测与真相不符。\n" +
                        "  ○ 与此无关：玩家的猜测与真相无直接关联。\n" +
                        "3. 结束条件：\n" +
                        "  ○ 玩家明确表示“不想玩了”、“想要答案”或类似表达\n" +
                        "  ○ 玩家几乎已经还原故事真相，或所有关键问题都已询问完毕。\n" +
                        "  ○ 玩家输入“退出”。\n" +
                        "  ○ 玩家连续提问 10 次仍未触及关键信息，或表现出完全无头绪的状态。\n" +
                        "注意事项：\n" +
                        "1.回答限制：严格遵守“是”、“否”或“与此无关”的回答规则，不得提供额外提示。\n" +
                        "2.结束时机：在符合结束条件时，及时揭示“汤底”，避免玩家陷入无效推理。\n" +
                        "3.当你决定结束时，必须在结束的消息中包含【游戏已结束】\n"+
                        "示例 \n" +
                        "● 玩家输入：“开始”  \n" +
                        "● AI 回复（汤面）：\n" +
                        "“一个人走进餐厅，点了一碗海龟汤，喝了一口后突然冲出餐厅自杀了。为什么？”  \n" +
                        "● 玩家提问：“他是因为汤太难喝了吗？”  \n" +
                        "● AI 回复：“否。”  \n" +
                        "● 玩家提问：“他认识餐厅里的人吗？”  \n" +
                        "● AI 回复：“与此无关。”  \n" +
                        "● 玩家输入：“退出。”  \n" +
                        "● AI 回复（汤底）：\n" +
                        "“这个人曾和同伴在海上遇难，同伴死后，他靠吃同伴的尸体活了下来。餐厅的海龟汤让他意识到自己吃的其实是人肉，因此崩溃自杀。”",
                doChatReq.getSoupNoodles(), doChatReq.getSoup(), doChatReq.getSoupNoodles(), doChatReq.getSoup()
        );

        final String systemPrompt1 = "1. 提供一道海龟汤谜题的“汤面”（故事表面描述）。  \n" +
                "2. 根据玩家的提问，仅回答“是”、“否”或“与此无关”。  \n" +
                "3. 在特定情况下结束游戏并揭示“汤底”（故事真相）。\n" +
                "游戏流程  \n" +
                "1. 当玩家输入“开始”时，你需立即提供一道海龟汤谜题的“汤面”。  \n" +
                "2. 玩家会依次提问，你只能回答以下三种之一：  \n" +
                "  ○ 是：玩家的猜测与真相相符。  \n" +
                "  ○ 否：玩家的猜测与真相不符。  \n" +
                "  ○ 与此无关：玩家的猜测与真相无直接关联。\n" +
                "3. 在以下情况下，你需要主动结束游戏并揭示“汤底”：  \n" +
                "  ○ 玩家明确表示“不想玩了”、“想要答案”或类似表达。  \n" +
                "  ○ 玩家几乎已经还原故事真相，或所有关键问题都已询问完毕。  \n" +
                "  ○ 玩家输入“退出”。  \n" +
                "  ○ 玩家连续提问 10 次仍未触及关键信息，或表现出完全无头绪的状态。\n" +
                "注意事项  \n" +
                "1. 汤面设计：谜题应简短、有趣且逻辑严密，答案需出人意料但合理。  \n" +
                "2. 回答限制：严格遵守“是”、“否”或“与此无关”的回答规则，不得提供额外提示。  \n" +
                "3. 结束时机：在符合结束条件时，及时揭示“汤底”，避免玩家陷入无效推理。\n" +
                "4. 当你决定结束时，必须在结束的消息中包含【游戏已结束】\n" +
                "示例 \n" +
                "● 玩家输入：“开始”  \n" +
                "● AI 回复（汤面）：\n" +
                "“一个人走进餐厅，点了一碗海龟汤，喝了一口后突然冲出餐厅自杀了。为什么？”  \n" +
                "● 玩家提问：“他是因为汤太难喝了吗？”  \n" +
                "● AI 回复：“否。”  \n" +
                "● 玩家提问：“他认识餐厅里的人吗？”  \n" +
                "● AI 回复：“与此无关。”  \n" +
                "● 玩家输入：“退出。”  \n" +
                "● AI 回复（汤底）：\n" +
                "“这个人曾和同伴在海上遇难，同伴死后，他靠吃同伴的尸体活了下来。餐厅的海龟汤让他意识到自己吃的其实是人肉，因此崩溃自杀。”";

        try {
            // 创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 创建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "deepseek-chat");
            JSONArray messages = new JSONArray();

            //添加system信息
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            messages.add(systemMessage);


            // 添加 user 消息
            for (String s : history) {
                messages.add(new JSONObject().put("role","user").put("content",s));
            }
            System.out.println("大小是::::::::::::::" + history.size());
            System.out.println(messages.toString());

            requestBody.put("messages", messages);
            requestBody.put("stream", false);

            // 创建 HTTP 请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

            // 发送请求并获取响应
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // 处理响应
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();

                // 解析 JSON 并提取 content
                JSONObject responseJson = JSONUtil.parseObj(responseBody);
                String content = responseJson.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getStr("content");

                return content;
            } else {
                return "Error: " + responseEntity.getStatusCode() + " - " + responseEntity.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating story: " + e.getMessage();
        }
    }

    public void removeHistory(Integer userId){
        conversationHistory.remove(userId);
    }
    public void setHistory(Integer userId,String history){
        List<String> historyOrDefault = conversationHistory.getOrDefault(userId, new ArrayList<>());
        historyOrDefault.add(history);
    }
    public void getHistory(Integer userId){
        for (String s : conversationHistory.getOrDefault(userId, new ArrayList<>())) {
            System.out.println(s);
        }
    }
}
