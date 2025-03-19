package com.sea.turtle.soup.turup.service;

import com.sea.turtle.soup.turup.dto.resp.WxLoginResponse;

/**
 * 微信认证服务接口
 */
public interface WxAuthService {
    
    /**
     * 微信登录
     * @param code 微信登录code
     * @param nickname 用户昵称
     * @param avatar 用户头像
     * @return 登录响应信息，包含token等数据
     * @throws RuntimeException 当登录失败时抛出异常
     */
    WxLoginResponse login(String code, String nickname, String avatar);

    /**
     * 根据token获取用户ID
     * @param token JWT token
     * @return 用户ID，如果token无效则返回null
     */
    Integer getUserIdByToken(String token);
} 