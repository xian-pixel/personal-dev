package vo;

import lombok.Builder;
import lombok.Data;

/**
 *  返回结果封装类
 * @param <T>
 */
@Data//lombok注解，自动生成getter和setter方法
@Builder
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;
}
