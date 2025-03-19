package com.sea.turtle.soup.turup.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sea.turtle.soup.turup.context.GameContext;
import com.sea.turtle.soup.turup.context.GameContextHolder;
import com.sea.turtle.soup.turup.dao.entity.User;
import com.sea.turtle.soup.turup.dao.mapper.UserMapper;
import com.sea.turtle.soup.turup.dto.resp.WxLoginResponse;
import com.sea.turtle.soup.turup.service.WxAuthService;
import com.sea.turtle.soup.turup.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class WxAuthServiceImpl implements WxAuthService {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.login-url}")
    private String loginUrl;

    private final UserMapper userMapper;
    private final TokenUtil tokenUtil;

    public WxAuthServiceImpl(UserMapper userMapper, TokenUtil tokenUtil) {
        this.userMapper = userMapper;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public WxLoginResponse login(String code, String nickname, String avatar) {
        // 1. 构造请求URL
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                loginUrl, appid, secret, code);
        System.out.println(url);
        // 2. 发送HTTP GET请求
        String response = HttpUtil.get(url);
        JSONObject wxJson = JSONUtil.parseObj(response);
        System.out.println(wxJson);
        
        // 3. 处理返回结果
        if (wxJson.containsKey("errcode") && !"0".equals(wxJson.getStr("errcode"))) {
            log.error("微信登录失败：{}", wxJson.getStr("errmsg"));
            throw new RuntimeException("微信登录失败：" + wxJson.getStr("errmsg"));
        }
        
        // 4. 获取openid
        String openid = wxJson.getStr("openid");
        if (openid == null || openid.isEmpty()) {
            throw new RuntimeException("获取openid失败");
        }

        // 5. 检查用户是否存在，不存在则创建
        User user = userMapper.findByOpenid(openid);
        if (user == null) {
            // 新用户，创建用户信息
            user = User.builder()
                    .openid(openid)
                    .nickname(nickname)
                    .avatar(avatar)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insertUser(user);
        } else {
            // 更新用户信息
            user.setNickname(nickname);
            user.setAvatar(avatar);
            userMapper.updateUser(user);
        }

        // 6. 生成JWT token并返回登录响应
        String token = tokenUtil.generateToken(user.getId());

        return WxLoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .expiresIn(30L * 24 * 60 * 60) // 30天的秒数
                .build();
    }

    @Override
    public Integer getUserIdByToken(String token) {
        return tokenUtil.getUserIdFromToken(token);
    }
} 