package com.server.server.controller;

import com.server.server.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@CrossOrigin
public class FileController {

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "上传文件不能为空");
        }

        // 确保目录存在
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        try {
            file.transferTo(new File(folder.getAbsolutePath() + File.separator + fileName));
            // 返回文件的可访问路径 (相对路径)
            String fileUrl = "/api/file/view/" + fileName;
            return Result.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(500, "文件上传失败：" + e.getMessage());
        }
    }
}
