package com.zhengyang.context;

/**
 * 登录用户上下文：通过拦截器写入，业务代码随时读取（基于 ThreadLocal）
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    public static void set(Long userId, String username) {
        USER_ID.set(userId);
        USERNAME.set(username);
    }

    /**
     * 获取当前登录用户的 userId
     * @return
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 获取当前登录用户的用户名
     * @return
     */
    public static String getUsername() {
        return USERNAME.get();
    }

    /** 请求结束时清理，防止线程复用导致内存泄漏/串号 */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
    }
}
