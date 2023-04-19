package com.klbr184.service;

import com.klbr184.resp.CommonResp;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-15 01:46:05
 */
public interface StatisticService {
    CommonResp getBasicStatistic();

    CommonResp getIndexStatistic();
}
