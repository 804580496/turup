package com.sea.turtle.soup.turup.controller;

import com.github.pagehelper.PageInfo;
import com.sea.turtle.soup.turup.dto.resp.RecordResponse;
import com.sea.turtle.soup.turup.dto.resp.UserResponse;
import com.sea.turtle.soup.turup.service.RecordService;
import com.sea.turtle.soup.turup.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/record/{userId}")
    public PageInfo<RecordResponse> getUserGameRecordsByPage(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return recordService.getUserGameRecordsByPage(userId, pageNum, pageSize);
    }

    @GetMapping("/userInfo/{openid}")
    public Integer getUserIdByOpenId(@PathVariable String openid){
        int id = recordService.getUserIdByOpenid(openid);
        return id;
    }
}
