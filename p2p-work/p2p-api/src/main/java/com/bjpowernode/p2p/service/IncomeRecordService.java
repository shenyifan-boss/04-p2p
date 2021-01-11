package com.bjpowernode.p2p.service;

import java.math.BigDecimal;

/**
 * 奕凡
 * 2020/12/27
 **/
public interface IncomeRecordService {
    /** 查询累计成交金额为主页
     * @return BigDecimal
     */
    BigDecimal selectMoneyForIndex();

    /**
     * 生成收益计划
     */
    void generateIncomePan();

    /**
     * 收益返还
     */
    void generateIncomeBack();
}
