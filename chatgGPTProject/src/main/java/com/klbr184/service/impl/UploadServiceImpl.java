package com.klbr184.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.klbr184.entity.UserEntity;
import com.klbr184.exception.SystemException;
import com.klbr184.mapper.UserMapper;
import com.klbr184.req.AiSettingReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.UploadService;
import com.klbr184.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-06 01:21:03
 */
@Service
@ConfigurationProperties(prefix = "oss")
public class UploadServiceImpl implements UploadService {
    @Autowired
    private UserMapper userMapper;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String endpoint;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public CommonResp uploadAvatar(MultipartFile file) {
        //TODO 判断文件大小
        //判断文件是否为空
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new SystemException(500, "上传头像失败,文件为空");
        }
        //判断文件类型
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new SystemException(500, "上传头像失败,文件类型错误");
        }
        //上传头像图片到阿里云OSS
        String fileName;
        //判断文件名是否够6位，文件名为UUID+原本文件名的后十(最多)位ID
        if (file.getOriginalFilename().length() < 10) {
            fileName = IdUtil.simpleUUID() + '-' + file.getOriginalFilename();
        } else {
            fileName = IdUtil.simpleUUID() + '-' + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 10);
        }
        String objectName = "avatar/" + fileName;
        //创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, objectName, file.getInputStream());
            //对头像进行缩放
            String style = "image/resize,m_fixed,w_100,h_100";
            //更新数据库中的头像地址
            UserEntity user = UserEntity.builder()
                    .id(SecurityUtil.getUserId())
                    .head(fileName)
                    .build();
            userMapper.updateById(user);
            return new CommonResp(200, "上传头像成功", fileName);
        } catch (OSSException oe) {
            throw new SystemException(500, "上传头像失败,远程服务器错误");
        } catch (ClientException ce) {
            throw new SystemException(500, "上传头像失败,客户端错误");
        } catch (FileNotFoundException e) {
            throw new SystemException(500, "上传头像失败,文件未找到");
        } catch (IOException e) {
            throw new SystemException(500, "头像上传失败,文件读取错误");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


}
