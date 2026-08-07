package com.zhengyang.controller;

import base.StatusCode;
import com.zhengyang.service.Loginservice;
import dto.UserLoginDTO;
import entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.ResultVO;

@RestController
@RequestMapping("/login")
@Tag(name = "登录接口")
public class LoginController {
    private  final Loginservice loginService;
    //构造器注入
    public LoginController(Loginservice loginService){
        this.loginService = loginService;
    }
    @PostMapping("/login")
    @Operation(summary = "登录接口")
    public ResultVO<String> login(String username,String password ) {
        //调用登录服务，拿到真实 token
        String token = loginService.login(username,password);
        if (token == null) {
            return ResultVO.<String>builder()
                    .code(StatusCode.FAIL.getCode())
                    .msg("用户名或密码错误")
                    .build();
        }
        return ResultVO.<String>builder()
                .code(StatusCode.SUCCESS.getCode())
                .msg("登录成功")
                .data(token)
                .build();
    }

    //注册接口
    @PostMapping("/register")
    @Operation(summary = "注册接口")
    public ResultVO<String> register(@RequestBody UserLoginDTO userLoginDTO) {
        //调用注册服务，注册用户
        UserLoginDTO user = loginService.register(userLoginDTO);
        if(user == null){
            return ResultVO.<String>builder()
                    .code(StatusCode.FAIL.getCode())
                    .msg("注册失败，用户名已存在")
                    .build();
        }
        return ResultVO.<String>builder()
                .code(StatusCode.SUCCESS.getCode())
                .msg("注册成功")
                .data(user.toString())
                .build();

    }

}
