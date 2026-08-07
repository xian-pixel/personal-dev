package com.zhengyang.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类：生成 / 解析 / 校验 token
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
 * @ r 获取 token 过期时间，单位毫秒
     * @return
     */
    public Long getExpiration() {
        return expiration;
    }

    /** 根据配置的 secret 生成符合长度的 HMAC 密钥 */
    private SecretKey getSigningKey() {
        // jjwt 要求 HMAC 密钥至少 256 位(32字节)，不足时以 SHA-256 哈希补齐
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            try {
                keyBytes = java.security.MessageDigest.getInstance("SHA-256")
                        .digest(keyBytes);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("获取 SHA-256 算法实例失败",e);
            }
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 token
     * @param userId   用户ID
     * @param username 用户名
     * @return token 字符串
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        Date now = new Date();
        Date expire = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expire)
                .signWith(getSigningKey())
                .compact();
    }

    /** 解析 token，返回 Claims（签名/过期错误会抛异常） */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** 从 token 中取出用户名 */
    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    /** 从 token 中取出用户ID */
    public Long getUserId(String token) {
        return parseToken(token).get("userId", Long.class);
    }

    /** 校验 token 是否合法（签名正确且未过期） */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
