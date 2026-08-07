package com.zhengyang.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhengyang.mapping.UserMapper;
import com.zhengyang.service.Loginservice;
import com.zhengyang.util.PasswordUtil;
import com.zhengyang.util.TokenService;
import dto.UserLoginDTO;
import entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements Loginservice {

    private final TokenService tokenService;
    private final PasswordUtil passwordUtil;

    // 构造器注入 TokenService（避免自注入，登录逻辑依赖 token 生成）
    public LoginServiceImpl(TokenService tokenService, PasswordUtil passwordUtil) {
        this.tokenService = tokenService;
        this.passwordUtil = passwordUtil;
    }

    @Override
    public String login(String username,String password) {
        /**
         * 1. 根据用户名查询用户信息
         * 2. 校验密码
         * 3. 生成 token 并写入 Redis
         * 4. 返回 token
         */
        // 1. 根据用户名查询用户信息（注意：不是用 id 查，是用 username 查）
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (isUserEmpty(user)) {
            log.info("用户不存在");
            return null;
        }
        // 2. 校验密码
        //通过PasswordUtil校验密码是否正确
        if (!passwordUtil.matches(password, user.getPassword())) {
            log.error("密码错误");
            return null;
        }
        // 3. 生成 token 并写入 Redis（同时支持登出/改密即时失效）
        String token = tokenService.createAndStoreToken(user.getId(), user.getUsername());
        // 4. 返回 token
        log.info("登录成功，token：{}", token);
        return token;
    }

    /**
     * 注册方法
     * @param userLoginDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserLoginDTO register(UserLoginDTO userLoginDTO) {
        User user = new User();
        BeanUtils.copyProperties(userLoginDTO, user);//将 UserLoginDTO 转换为 User 对象
        //校验用户名是否已经存在
        User existingUser = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if(!isUserEmpty(existingUser)){
            log.info("用户名已存在");
            return null;
        }
        //将密码加密后存储到数据库
        user.setPassword(passwordUtil.encode(user.getPassword()));
        //设置默认状态为正常
        user.setStatus("1");
        //设置默认删除状态为未删除
        user.setDeleted("0");
        //设置默认创建时间和更新时间为当前时间
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        this.save(user);//保存用户到数据库
        //将User对象转换为UserLoginDTO对象
        BeanUtils.copyProperties(user, userLoginDTO);//将User对象的属性复制到UserLoginDTO对象
        // 返回注册成功的用户信息（包含 id、username、password 等）
        return userLoginDTO;
    }

    /**
     * 判断用户是否为空
     */
    public Boolean isUserEmpty(User user){
        return user == null;
    }
}
