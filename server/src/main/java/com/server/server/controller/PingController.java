package com.server.server.controller;

import com.server.server.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PingController {

    @GetMapping("/ping")
    public Result<Map<String, Object>> ping() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("time", LocalDateTime.now());
        data.put("message", "后端服务运行正常，对接成功！");
        return Result.success(data);
    }
}
