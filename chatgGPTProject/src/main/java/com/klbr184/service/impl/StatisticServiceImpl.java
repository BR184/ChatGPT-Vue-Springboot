package com.klbr184.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.klbr184.exception.SystemException;
import com.klbr184.mapper.AiSettingMapper;
import com.klbr184.mapper.ChatMapper;
import com.klbr184.mapper.UserChatMapper;
import com.klbr184.mapper.UserMapper;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.StatisticService;
import com.klbr184.utils.RedisUtil;
import com.klbr184.vo.BasicStatisticVo;
import com.klbr184.vo.IndexStatisticVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-15 01:47:01
 */
@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AiSettingMapper aiSettingMapper;
    @Autowired
    private UserChatMapper userChatMapper;
    @Override
    public CommonResp getBasicStatistic() {
        BasicStatisticVo basicStatisticVo = new BasicStatisticVo();
        //设置一个查询条件，查询今日的数据
        QueryWrapper queryTodayOnly = new QueryWrapper();
        try {
            //查询今日新增用户
            queryTodayOnly.apply(true, "TO_DAYS(NOW())-TO_DAYS(reg_time) = 0");
            basicStatisticVo.setTodayNewUser(userMapper.selectList(queryTodayOnly).size());
            //查询今日新增设定
            queryTodayOnly.clear();
            queryTodayOnly.apply(true, "TO_DAYS(NOW())-TO_DAYS(create_time) = 0");
            basicStatisticVo.setTodayNewAiSetting(aiSettingMapper.selectList(queryTodayOnly).size());
            //查询今日新增聊天
            basicStatisticVo.setTodayNewChat(userChatMapper.selectList(queryTodayOnly).size());
            //查询当前登录用户
            basicStatisticVo.setLoginUsers(RedisUtil.countKeysStartWith("login"));
        } catch (Exception e) {
            throw new SystemException(500, "查询失败: " + e.getMessage());
        }
        return new CommonResp<>(200, "操作成功", basicStatisticVo);
    }

    @Override
    public CommonResp getIndexStatistic() {
        IndexStatisticVo indexStatisticVo = new IndexStatisticVo();
        try {
            //查询所有设定的数量
            indexStatisticVo.setTotalAiSettingNum(aiSettingMapper.selectList(null).size());
            //查询所有聊天的数量
            indexStatisticVo.setTotalNewChatNum(userChatMapper.selectList(null).size());
            //查询所有分享的数量
            indexStatisticVo.setTotalNewShareNum(0);
        } catch (Exception e) {
            throw new SystemException(500, "查询失败: " + e.getMessage());
        }
        return new CommonResp<>(200, "操作成功", indexStatisticVo);
    }
}
