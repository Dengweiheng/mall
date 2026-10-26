package com.server.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 全局跨域配置，对接前端使用
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @org.springframework.beans.factory.annotation.Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问的路径
                .allowedOriginPatterns("*") // 允许跨域访问的源 (Vue3 默认 5173)
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许请求方法
                .maxAge(3600) // 预检间隔时间
                .allowCredentials(true); // 是否允许携带 cookie
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        // 将 /api/file/view/** 映射到本地上传目录
        String path = new File(uploadPath).getAbsolutePath() + File.separator;
        registry.addResourceHandler("/api/file/view/**")
                .addResourceLocations("file:" + path);
    }
}
