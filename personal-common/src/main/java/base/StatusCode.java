package base;

public enum StatusCode {
    SUCCESS(200, "登录成功"),
    FAIL(500, "登录失败"),
    UNAUTHORIZED(401, "用户未授权"),
    FORBIDDEN(403, "用户无权限"),
    ERRORUSERLOGIN(500, "无此用户");



    private final int code;//状态码
    private final String msg;//状态码描述

    /**
     * 构造方法
     * @param code
     * @param msg
     */
    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     *  获取状态码
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     *  获取状态码描述
     * @return
     */
    public String getMsg() {
        return msg;
    }
}
