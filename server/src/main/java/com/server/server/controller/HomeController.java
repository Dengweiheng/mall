package com.server.server.controller;

import com.server.server.common.Result;
import com.server.server.entity.dto.HomeDataDTO;
import com.server.server.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@CrossOrigin
public class HomeController {

    @Autowired
    private HomeService homeService;

    /**
     * 获取首页看板统计数据
     */
    @GetMapping("/statistics")
    public Result<HomeDataDTO> getStatistics() {
        HomeDataDTO data = homeService.getHomeStatistics();
        return Result.success(data);
    }
}
