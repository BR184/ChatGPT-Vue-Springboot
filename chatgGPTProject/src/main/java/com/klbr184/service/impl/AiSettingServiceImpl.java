package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klbr184.entity.AiSetting;
import com.klbr184.exception.SystemException;
import com.klbr184.mapper.AiSettingMapper;
import com.klbr184.mapper.UserMapper;
import com.klbr184.req.AiSettingReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.AiSettingService;
import com.klbr184.utils.SecurityUtil;
import com.klbr184.vo.AiSettingsListVo;
import com.klbr184.vo.AiSettingsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-07 16:49:59
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AiSettingServiceImpl implements AiSettingService {
    @Autowired
    private AiSettingMapper aiSettingMapper;
    @Autowired
    private UserMapper userMapper;

    //创建系统设定
    @Override
    public CommonResp createSystem(AiSettingReq req) {
        //判断是否有参数
        if (Objects.isNull(req) || req.getValue() == null || req.getValue().equals("")) {
            throw new SystemException(500, "上传系统失败,参数为空");
        }
        //判断是否有简介
        if (Objects.isNull(req.getIntro()) || req.getIntro().equals("")) {
            //没有标题则使用默认简介
            req.setIntro("新设定");
        }
        //拷贝对象
        AiSetting aiSetting = new AiSetting();
        BeanUtil.copyProperties(req, aiSetting);
        //补全属性
        aiSetting.setUserId(SecurityUtil.getUserId())
                .setCreateTime(DateUtil.date());
        //保存到数据库
        aiSettingMapper.insert(aiSetting);
        return new CommonResp<>(200, "操作成功", null);
    }

    @Override
    public CommonResp getSystem() {
        //获取用户id
        Long userId = SecurityUtil.getUserId();
        //查询数据库
        List<AiSettingsVo> aiSettings = aiSettingMapper.selectListByUserId(userId);
        return new CommonResp<>(200, "操作成功", aiSettings);
    }

    @Override
    public CommonResp deleteSystem(Long id) {
        //判断是否有参数
        if (id == null || id.equals("")) {
            throw new SystemException(500, "删除系统失败,参数为空");
        }
        //删除数据库
        aiSettingMapper.deleteById(id);
        return new CommonResp<>(200, "操作成功", null);
    }

    @Override
    public CommonResp getSystemById(Long id) {
        //判断是否有参数
        if (id == null || id.equals("")) {
            throw new SystemException(500, "获取系统失败,参数为空");
        }
        //查询数据库
        AiSetting aiSetting = aiSettingMapper.selectById(id);
        //拷贝对象
        AiSettingsVo aiSettings = new AiSettingsVo();
        BeanUtil.copyProperties(aiSetting, aiSettings);
        return new CommonResp<>(200, "操作成功", aiSettings);
    }

    @Override
    public CommonResp updateSystem(AiSettingReq req) {
        //判断是否有参数
        if (Objects.isNull(req) || req.getValue() == null || req.getValue().equals("")) {
            throw new SystemException(500, "上传系统失败,缺失参数");
        }
        //判断是否有简介
        if (Objects.isNull(req.getIntro()) || req.getIntro().equals("")) {
            //没有标题则使用默认简介
            req.setIntro("新设定");
        }
        //拷贝对象
        AiSetting aiSetting = new AiSetting();
        BeanUtil.copyProperties(req, aiSetting);
        //补全属性
        aiSetting.setUpdateTime(DateUtil.date());
        System.out.println(aiSetting);
        //保存到数据库
        aiSettingMapper.updateById(aiSetting);
        return new CommonResp<>(200, "操作成功", null);
    }

    @Override
    public CommonResp getSystemList(Integer page) {
        IPage<AiSetting> aiSettingIPage = new Page<>(page, 18);
        QueryWrapper<AiSetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AiSetting::getShared, 1)
                .select().orderByDesc(AiSetting::getCreateTime);
        aiSettingMapper.selectPage(aiSettingIPage, queryWrapper);
        AiSettingsListVo aiSettingsListVo = new AiSettingsListVo();
        aiSettingsListVo.setPage((int) aiSettingIPage.getCurrent());
        aiSettingsListVo.setTotalPage((int) aiSettingIPage.getPages());
        List<AiSettingsVo> list = new ArrayList<>();
        for (AiSetting record : aiSettingIPage.getRecords()) {
            AiSettingsVo aiSettingsVo = new AiSettingsVo();
            BeanUtil.copyProperties(record, aiSettingsVo);
            //获取用户名
            aiSettingsVo.setUsername(userMapper.selectUsernameById(record.getUserId()));
            aiSettingsVo.setUserId(record.getUserId());
            list.add(aiSettingsVo);
        }
        aiSettingsListVo.setList(list);
        return new CommonResp<>(200, "操作成功", aiSettingsListVo);
    }

    @Override
    public CommonResp saveSystem(Long id) {
        //判断是否有参数
        if (id == null || id.equals("")) {
            throw new SystemException(500, "保存系统失败,参数为空");
        }
        //获取原先的系统设定
        AiSetting aiSetting = aiSettingMapper.selectById(id);
        //判断是否是自己的
        if (aiSetting.getUserId().equals(SecurityUtil.getUserId())) {
            throw new SystemException(500, "保存系统失败,不能保存自己的系统设定");
        }
        //使用量+1
        aiSetting.setFav(aiSetting.getFav() + 1);
        //保存到数据库
        aiSettingMapper.updateById(aiSetting);
        //保存自己的一份
        aiSetting.setUserId(SecurityUtil.getUserId())
                .setCreateTime(DateUtil.date())
                .setFav(0)
                .setShared(0)
                .setId(IdUtil.getSnowflakeNextId());
        aiSettingMapper.insert(aiSetting);
        return new CommonResp<>(200, "操作成功", null);
    }

}
