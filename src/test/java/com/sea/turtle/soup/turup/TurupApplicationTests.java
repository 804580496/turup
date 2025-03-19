package com.sea.turtle.soup.turup;

import cn.hutool.core.date.DateUnit;
import com.sea.turtle.soup.turup.api.DeepSeekClient;
import com.sea.turtle.soup.turup.context.GameContext;
import com.sea.turtle.soup.turup.context.GameContextHolder;
import com.sea.turtle.soup.turup.dao.entity.User;
import com.sea.turtle.soup.turup.dao.mapper.UserMapper;
import com.sea.turtle.soup.turup.dto.req.DoChatReq;
import com.sea.turtle.soup.turup.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class TurupApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeepSeekClient deepSeekClient;
    @Autowired
    private TokenUtil tokenUtil;
    @Test
    void contextLoads() {
        GameContextHolder.setContext(new GameContext(4,3));
        DoChatReq doChatReq = DoChatReq.builder()
                .messages("")
                .soupNoodles("著名钢琴家在音乐会前失踪，警方在后台发现一把沾满血迹的钢琴椅和一张撕碎的乐谱。奇怪的是，钢琴完好无损，琴键上却沾满了盐粒。")
                .soup("钢琴家其实是被他的双胞胎兄弟杀害并分尸。兄弟将尸体藏在钢琴内部，用盐粒掩盖血腥味。撕碎的乐谱是兄弟伪造的遗书，试图制造自杀假象。钢琴椅上的血迹是分尸时留下的，而钢琴完好无损是因为兄弟本身就是一位技艺高超的钢琴技师，能够完美拆卸和重组钢琴。")
                .build();

        String s = deepSeekClient.doChat(doChatReq);
        System.out.println(s);
        DoChatReq doChatReq1 = DoChatReq.builder()
                .messages("开始")
                .soupNoodles("著名钢琴家在音乐会前失踪，警方在后台发现一把沾满血迹的钢琴椅和一张撕碎的乐谱。奇怪的是，钢琴完好无损，琴键上却沾满了盐粒。")
                .soup("钢琴家其实是被他的双胞胎兄弟杀害并分尸。兄弟将尸体藏在钢琴内部，用盐粒掩盖血腥味。撕碎的乐谱是兄弟伪造的遗书，试图制造自杀假象。钢琴椅上的血迹是分尸时留下的，而钢琴完好无损是因为兄弟本身就是一位技艺高超的钢琴技师，能够完美拆卸和重组钢琴。")
                .build();
        String s1 = deepSeekClient.doChat(doChatReq1);
        System.out.println(s1);
        DoChatReq doChatReq2 = DoChatReq.builder()
                .messages("是被吃饭的时候盐撒了？")
                .soupNoodles("著名钢琴家在音乐会前失踪，警方在后台发现一把沾满血迹的钢琴椅和一张撕碎的乐谱。奇怪的是，钢琴完好无损，琴键上却沾满了盐粒。")
                .soup("钢琴家其实是被他的双胞胎兄弟杀害并分尸。兄弟将尸体藏在钢琴内部，用盐粒掩盖血腥味。撕碎的乐谱是兄弟伪造的遗书，试图制造自杀假象。钢琴椅上的血迹是分尸时留下的，而钢琴完好无损是因为兄弟本身就是一位技艺高超的钢琴技师，能够完美拆卸和重组钢琴。")
                .build();
        String s2 = deepSeekClient.doChat(doChatReq2);
        System.out.println(s2);
        DoChatReq doChatReq3 = DoChatReq.builder()
                .messages("钢琴家其实是被他的双胞胎兄弟杀害并分尸。兄弟将尸体藏在钢琴内部，用盐粒掩盖血腥味。撕碎的乐谱是兄弟伪造的遗书，试图制造自杀假象。钢琴椅上的血迹是分尸时留下的，而钢琴完好无损是因为兄弟本身就是一位技艺高超的钢琴技师，能够完美拆卸和重组钢琴")
                .soupNoodles("著名钢琴家在音乐会前失踪，警方在后台发现一把沾满血迹的钢琴椅和一张撕碎的乐谱。奇怪的是，钢琴完好无损，琴键上却沾满了盐粒。")
                .soup("钢琴家其实是被他的双胞胎兄弟杀害并分尸。兄弟将尸体藏在钢琴内部，用盐粒掩盖血腥味。撕碎的乐谱是兄弟伪造的遗书，试图制造自杀假象。钢琴椅上的血迹是分尸时留下的，而钢琴完好无损是因为兄弟本身就是一位技艺高超的钢琴技师，能够完美拆卸和重组钢琴。")
                .build();
        String s3 = deepSeekClient.doChat(doChatReq3);
        System.out.println(s3);
    }

}
