package dto;

import entity.User;
import lombok.Data;

/**
 * 用户登录DTO
 */
@Data
public class UserLoginDTO extends User {
    /**
     * toString方法
     */
    @Override
    public String toString() {
        return "UserLoginDTO{" +
                ", username='" + super.getUsername() + '\'' +
                ", password='" + super.getPassword() + '\'' +
                ", email='" + super.getEmail() + '\'' +
                ", nickname='" + super.getNickname() + '\'' +
                ", phone='" + super.getPhone() + '\'' +
                ", createTime=" + super.getCreateTime() +
                ", updateTime=" + super.getUpdateTime() +
                '}';
    }
}
