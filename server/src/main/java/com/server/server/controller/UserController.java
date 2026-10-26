package com.server.server.controller;

import com.server.server.common.PageResult;
import com.server.server.common.Result;
import com.server.server.entity.User;
import com.server.server.entity.dto.PasswordDTO;
import com.server.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin // 简单允许跨域，实际生产中可以配置全局CORS
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        log.info("User login attempt: {}", user.getUsername());
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        log.info("User login successful: {}", loginUser.getUsername());
        return Result.success(loginUser);
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    public Result<PageResult<User>> findPage(
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String realName,
            @RequestParam(defaultValue = "") String role,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<User> pageInfo = userService.findPage(username, realName, role, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 获取单个用户
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 修改用户基本信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody PasswordDTO pwdObj) {
        if (pwdObj.getId() == null || pwdObj.getOldPassword() == null || pwdObj.getNewPassword() == null) {
            return Result.error(400, "参数不完整");
        }
        userService.updatePassword(pwdObj.getId(), pwdObj.getOldPassword(), pwdObj.getNewPassword());
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 重置用户密码 (管理员功能)
     */
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        // 重置为默认密码 123456
        userService.resetPassword(id, "123456");
        return Result.success();
    }

    /**
     * 更新用户状态 (启用/禁用)
     */
    @PutMapping("/{id}/status/{status}")
    public Result<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }
}
