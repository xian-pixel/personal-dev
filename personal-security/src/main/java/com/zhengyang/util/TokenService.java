package com.zhengyang.util;

import com.zhengyang.util.JwtUtil;
import com.zhengyang.util.SecurityConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token 服务：负责登录时写 Redis、登出删 Redis、刷新校验
 * Redis 中存储：key = token:userId  value = jwtToken  并设置与 JWT 相同的过期时间
 */
@Service
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;

    public TokenService(RedisTemplate<String, Object> redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    /** 登录成功后，生成 token 并写入 Redis */
    public String createAndStoreToken(Long userId, String username) {
        String token = jwtUtil.generateToken(userId, username);
        String redisKey = String.format(SecurityConstants.REDIS_TOKEN_KEY, userId);
        // 过期时间取 JWT 配置的毫秒数
        redisTemplate.opsForValue().set(redisKey, token, jwtUtil.getExpiration(), TimeUnit.MILLISECONDS);
        return token;
    }

    /** 校验 token：1) JWT 本身合法 2) Redis 中仍存在且值一致（支持登出/改密即时失效） */
    public boolean verify(String token) {
        if (!jwtUtil.validateToken(token)) {
            return false;
        }
        Long userId = jwtUtil.getUserId(token);
        String redisKey = String.format(SecurityConstants.REDIS_TOKEN_KEY, userId);
        Object stored = redisTemplate.opsForValue().get(redisKey);
        return stored != null && stored.equals(token);
    }

    /** 登出：删除 Redis 中的 token，使其立即失效 */
    public void revoke(Long userId) {
        String redisKey = String.format(SecurityConstants.REDIS_TOKEN_KEY, userId);
        redisTemplate.delete(redisKey);
    }
}
