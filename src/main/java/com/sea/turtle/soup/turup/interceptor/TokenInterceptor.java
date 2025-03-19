package com.sea.turtle.soup.turup.interceptor;

import com.sea.turtle.soup.turup.context.GameContext;
import com.sea.turtle.soup.turup.context.GameContextHolder;
import com.sea.turtle.soup.turup.service.WxAuthService;
import com.sea.turtle.soup.turup.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private WxAuthService wxAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取 token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is missing");
            return false;
        }
        System.out.println(token);

        // 2. 解析 token 获取 userId
        Integer userId = wxAuthService.getUserIdByToken(token);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return false;
        }

        // 3. 将 userId 存入请求属性中，供后续使用
        GameContext gameContext = GameContext.builder()
                .userId(userId).build();
        GameContextHolder.setContext(gameContext);
        System.out.println("已设置useriId为：：：：：：：：：：" + userId);
        return true;
    }
}
