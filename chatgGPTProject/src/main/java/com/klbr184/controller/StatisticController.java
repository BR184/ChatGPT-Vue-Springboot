package com.klbr184.controller;

import com.klbr184.resp.CommonResp;
import com.klbr184.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-19 01:13:35
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;
    @GetMapping("index")
    public CommonResp getIndexStatistic() {
        return statisticService.getIndexStatistic();
    }
}
