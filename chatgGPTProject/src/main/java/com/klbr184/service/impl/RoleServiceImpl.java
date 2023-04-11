package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klbr184.entity.Role;
import com.klbr184.mapper.RoleMapper;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.RoleService;
import com.klbr184.vo.RoleBeanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 16:02:17
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public CommonResp getRole(Integer page) {
        if (page == null) {
            return new CommonResp<>(400, "参数错误", null);
        }
        IPage<Role> iPage = new Page<>(page, 10);
        roleMapper.selectPage(iPage, null);
        List<Role> records = iPage.getRecords();
        if (records == null || records.size() == 0) {
            return new CommonResp<>(200, "操作成功", null);
        }
        RoleBeanVo roleBeanVo = new RoleBeanVo();
        roleBeanVo.setData(records);
        roleBeanVo.setPage((int) iPage.getCurrent());
        roleBeanVo.setTotalpage((int) iPage.getPages());
        return new CommonResp<>(200, "操作成功", roleBeanVo);
    }
}
