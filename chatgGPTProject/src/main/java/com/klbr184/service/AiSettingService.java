package com.klbr184.service;

import com.klbr184.req.AiSettingReq;
import com.klbr184.resp.CommonResp;
import org.springframework.stereotype.Service;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-07 16:49:45
 */
public interface AiSettingService {
    CommonResp createSystem(AiSettingReq req);

    CommonResp getSystem();

    CommonResp deleteSystem(Long id);

    CommonResp getSystemById(Long id);

    CommonResp updateSystem(AiSettingReq req);

    CommonResp getSystemList(Integer page);

    CommonResp saveSystem(Long id);

    CommonResp getAllSystemListByPage(Integer page);

    CommonResp adminUpdateSystem(AiSettingReq req);

    CommonResp adminDeleteSystem(Long id);
}
