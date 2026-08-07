package com.zhengyang.util;

/**
 * 安全相关常量
 */
public final class SecurityConstants {
    private SecurityConstants() {}

    /** 登录接口（放行） */
    public static final String LOGIN_URL = "/login";

    /** 注册接口（放行，预留） */
    public static final String REGISTER_URL = "/register";

    /** Redis 中 token 的 key 格式：token:userId（配合 String.format 使用） */
    public static final String REDIS_TOKEN_KEY = "token:%s";

    /** 请求头名称 */
    public static final String HEADER_NAME = "access_token";

    /** token 前缀（Bearer 之类，可空） */
    public static final String TOKEN_PREFIX_BEARER = "access_token";
}
