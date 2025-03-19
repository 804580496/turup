package com.sea.turtle.soup.turup.controller;

import com.sea.turtle.soup.turup.dto.req.WxLoginReq;
import com.sea.turtle.soup.turup.dto.resp.WxLoginResponse;
import com.sea.turtle.soup.turup.service.RecordService;
import com.sea.turtle.soup.turup.service.WxAuthService;
import com.sea.turtle.soup.turup.util.Result;
import com.sea.turtle.soup.turup.util.TokenUtil;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/wx/auth")
public class WxAuthController {

    @Autowired
    private WxAuthService wxAuthService;
    private RecordService recordService;
    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 微信登录接口
     */
    @PostMapping("/login")
    public Result<WxLoginResponse> login(@RequestBody WxLoginReq wxLoginReq) {
        try {
            WxLoginResponse response = wxAuthService.login(wxLoginReq.getCode(), wxLoginReq.getNickname(), wxLoginReq.getAvatar());
            return Result.success(response);
        } catch (Exception e) {

            return Result.error(400, "登录失败: " + e.getMessage());
        }
    }

    /**
     * 退出登录接口
     */
    @PostMapping("/logout")
    public Result<Map<String, Object>> logout(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        Map<String, Object> response = new HashMap<>();

        if (token == null || token.isEmpty()) {
            return Result.error(400, "未提供token");
        }

        if (!tokenUtil.validateToken(token)) {
            return Result.error(401, "token无效或已过期");
        }

        return Result.success(null);
    }
} 