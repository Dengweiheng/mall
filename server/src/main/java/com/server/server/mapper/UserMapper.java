package com.server.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.server.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus 已经提供了基础的 CRUD 以及分页功能
    // custom methods if any can be kept here still
}
