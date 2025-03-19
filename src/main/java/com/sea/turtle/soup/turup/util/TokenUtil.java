package com.sea.turtle.soup.turup.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {
    
    @Value("${jwt.secret:turup_game_secret_key}")
    private String secretKey;  // JWT密钥，建议在配置文件中设置
    
    private static final long DEFAULT_EXPIRE_DAYS = 30;  // 默认token有效期30天

    /**
     * 生成默认有效期（30天）的JWT token
     */
    public String generateToken(Integer userId) {
        return generateToken(userId, DEFAULT_EXPIRE_DAYS);
    }

    /**
     * 生成指定有效期的JWT token
     * @param userId 用户ID
     * @param expireDays token有效期（天）
     */
    public String generateToken(Integer userId, long expireDays) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireDays * 24 * 60 * 60 * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)                // 设置自定义信息
                .setIssuedAt(now)                 // 设置签发时间
                .setExpiration(expireDate)        // 设置过期时间
                .signWith(key, SignatureAlgorithm.HS256)  // 设置签名
                .compact();
    }

    /**
     * 从token中获取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.get("userId", Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // token已过期
            return false;
        } catch (JwtException e) {
            // token解析失败
            return false;
        }
    }

    /**
     * 获取token的过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取token的剩余有效期（分钟）
     */
    public long getTokenRemainingTime(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration == null) {
            return 0;
        }
        
        long remainingMs = expiration.getTime() - System.currentTimeMillis();
        return remainingMs > 0 ? remainingMs / (60 * 1000) : 0;
    }

    /**
     * 检查token是否过期
     */
    public boolean isExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration == null || expiration.before(new Date());
    }
} 