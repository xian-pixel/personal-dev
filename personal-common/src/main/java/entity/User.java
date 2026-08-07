package entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")   // 对应数据库表名
public class User {
    @TableId(type = IdType.AUTO) // 主键策略为自增
    private Long id; //主键
    private String username; //用户名
    private String password; //密码
    private String nickname; //昵称
    private String avatar; //头像
    private String phone; //手机号
    private String email; //邮箱
    @JsonIgnore
    private String status; //状态 0-正常 1-禁用
    @JsonIgnore
    private LocalDateTime createTime; //创建时间
    @JsonIgnore
    private LocalDateTime updateTime; //更新时间
    @JsonIgnore
    private LocalDateTime lastLoginTime; //最后登录时间
    @JsonIgnore
    private String lastLoginIp; //最后登录ip
    @JsonIgnore
    private String deleted; //是否删除 0-未删除 1-已删除
}
