package com.klbr184.controller;

import com.klbr184.req.AiSettingReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-06 01:18:10
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    //上传头像
    @PreAuthorize("hasAuthority('default')")
    @PostMapping("/avatar")
    public CommonResp uploadAvatar(@RequestParam("file") MultipartFile file) {
        return uploadService.uploadAvatar(file);
    }



}
