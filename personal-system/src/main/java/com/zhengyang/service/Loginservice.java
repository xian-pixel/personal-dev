package com.zhengyang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import dto.UserLoginDTO;
import entity.User;

public interface Loginservice extends IService<User> {
    /**
     *  登录方法
     * @param username
     * @param password
     * @return
     */
    String login(String username,String password);

    /**
     *  注册方法
     * @param userLoginDTO
     * @return
     */
    UserLoginDTO register(UserLoginDTO userLoginDTO);
}
