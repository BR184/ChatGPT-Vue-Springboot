package com.klbr184.service;

import com.klbr184.resp.CommonResp;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-06 01:20:35
 */
public interface UploadService {
    CommonResp uploadAvatar(MultipartFile file);
}
