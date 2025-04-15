package com.itheima.controller;

import com.itheima.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    // 上传文件的方法
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        // 把文件的内容存储到本地磁盘上
        // 获取用户上传的原始文件名
        String originalFilename = file.getOriginalFilename();
        // 上传文件
        // 保证文件的名字是唯一的，从而防止文件覆盖
        // UUID+文件后缀名
        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        file.transferTo(new File("C:\\Users\\86153\\Desktop\\04_综合案例资料\\"+filename));
        return Result.success("url访问地址....");
    }

}
