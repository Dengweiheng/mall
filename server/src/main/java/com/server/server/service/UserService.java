package com.server.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.server.common.PageResult;
import com.server.server.entity.User;

public interface UserService extends IService<User> {
    
    /**
     * 用户登录验证
     */
    User login(String username, String password);

    /**
     * 注册用户
     */
    void register(User user);

    /**
     * 获取用户信息
     */
    User getById(Long id);

    /**
     * 分页查询
     */
    PageResult<User> findPage(String username, String realName, String role, int pageNum, int pageSize);

    /**
     * 修改用户信息
     */
    void update(User user);

    /**
     * 修改密码
     */
    void updatePassword(Long id, String oldPassword, String newPassword);

    /**
     * 重置用户密码 (管理员使用)
     */
    void resetPassword(Long id, String defaultPassword);

    /**
     * 更新用户状态 (启用/禁用)
     */
    void updateStatus(Long id, Integer status);

    /**
     * 删除用户
     */
    void deleteById(Long id);
}
