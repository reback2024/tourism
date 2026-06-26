package com.tourism.controller.admin;

import com.tourism.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class FileUploadController {

    @Value("${file.upload.path:./uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            file.transferTo(new File(dir, filename));
            return Result.ok(Map.of("url", "/uploads/" + filename));
        } catch (Exception e) {
            return Result.fail("上传失败: " + e.getMessage());
        }
    }
}
