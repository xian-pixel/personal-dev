package com.zhengyang.interceptors;

import com.zhengyang.context.UserContext;
import com.zhengyang.util.TokenService;
import com.zhengyang.util.JwtUtil;
import com.zhengyang.util.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器：校验请求头中的 token，并将用户信息写入 UserContext
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public JwtInterceptor(JwtUtil jwtUtil, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头取 token
        String token = request.getHeader(SecurityConstants.HEADER_NAME);
        if (token == null || token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未携带 token\"}");
            return false;
        }

        // 支持 "access_token xxxx" 形式，去掉前缀
        if (token.startsWith(SecurityConstants.TOKEN_PREFIX_BEARER)) {
            token = token.substring(SecurityConstants.TOKEN_PREFIX_BEARER.length()).trim();
        }

        // 先校验签名/过期，再校验 Redis 是否存在（双保险）
        if (!tokenService.verify(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"token 无效或已失效\"}");
            return false;
        }

        // 写入用户上下文，供后续业务使用
        UserContext.set(jwtUtil.getUserId(token), jwtUtil.getUsername(token));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束清理 ThreadLocal，避免线程复用串号
        UserContext.clear();
    }
}
