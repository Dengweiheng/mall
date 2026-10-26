package com.server.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.server.common.PageResult;
import com.server.server.entity.User;
import com.server.server.exception.CustomException;
import com.server.server.mapper.UserMapper;
import com.server.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new CustomException(400, "用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(400, "用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new CustomException(403, "当前账号已被禁用");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void register(User user) {
        User existingUser = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (existingUser != null) {
            throw new CustomException(400, "用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        user.setStatus(1);
        this.save(user);
    }

    @Override
    public User getById(Long id) {
        User user = super.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public PageResult<User> findPage(String username, String realName, String role, int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            wrapper.like(User::getUsername, username);
        }
        if (StringUtils.hasText(realName)) {
            wrapper.like(User::getRealName, realName);
        }
        if (StringUtils.hasText(role)) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> result = this.page(page, wrapper);
        
        List<User> list = result.getRecords();
        list.forEach(u -> u.setPassword(null));
        
        return new PageResult<>(result.getTotal(), list);
    }

    @Override
    public void update(User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        this.updateById(user);
    }

    @Override
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = super.getById(id);
        if (user == null) {
            throw new CustomException("找不到该用户");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
             throw new CustomException(400, "原密码错误");
        }
        User updateInfo = new User();
        updateInfo.setId(id);
        updateInfo.setPassword(passwordEncoder.encode(newPassword));
        this.updateById(updateInfo);
    }

    @Override
    public void resetPassword(Long id, String defaultPassword) {
        User user = super.getById(id);
        if (user == null) {
            throw new CustomException("找不到该用户");
        }
        User updateInfo = new User();
        updateInfo.setId(id);
        updateInfo.setPassword(passwordEncoder.encode(defaultPassword));
        this.updateById(updateInfo);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            throw new CustomException(400, "状态参数错误");
        }
        User updateInfo = new User();
        updateInfo.setId(id);
        updateInfo.setStatus(status);
        this.updateById(updateInfo);
    }

    @Override
    public void deleteById(Long id) {
        this.removeById(id);
    }
}
