package com.zhengyang.mapping;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper    // mybatis-plus 的映射注解
public interface UserMapper extends BaseMapper<User> {
}
