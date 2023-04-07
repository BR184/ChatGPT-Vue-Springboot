package com.klbr184.controller;

import com.klbr184.req.AiSettingReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.AiSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-07 16:47:04
 */
@RestController
@RequestMapping("/system")
public class AiSettingController {
    @Autowired
    private AiSettingService aiSettingService;
    //获取系统设定
    @PreAuthorize("hasAuthority('default')")
    @GetMapping()
    public CommonResp getSystem() {
        return aiSettingService.getSystem();
    }
    //上传系统设定
    @PreAuthorize("hasAuthority('default')")
    @PostMapping()
    public CommonResp uploadSystem(@RequestBody AiSettingReq req) {
        return aiSettingService.createSystem(req);
    }
    //删除系统设定
    @PreAuthorize("hasAuthority('default')")
    @DeleteMapping()
    public CommonResp deleteSystem(@RequestParam Long id) {
        return aiSettingService.deleteSystem(id);
    }
    //更新系统设定
    @PreAuthorize("hasAuthority('default')")
    @PutMapping()
    public CommonResp updateSystem(@RequestBody AiSettingReq req) {
        return aiSettingService.updateSystem(req);
    }

    //根据ID获取系统设定
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("get")
    public CommonResp getSystemById(@RequestParam Long id) {
        return aiSettingService.getSystemById(id);
    }

    //分页查询共享系统设定
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("shared")
    public CommonResp getSystemList(@RequestParam Integer page) {
        return aiSettingService.getSystemList(page);
    }

    //保存他人系统设定
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("save")
    public CommonResp saveSystem(@RequestParam Long id) {
        return aiSettingService.saveSystem(id);
    }
}
