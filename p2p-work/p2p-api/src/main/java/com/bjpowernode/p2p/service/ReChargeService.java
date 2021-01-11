package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.loan.RechargeRecord;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/7
 **/
public interface ReChargeService {

    /** 创建充值记录
     * @param rechargeRecord
     * @return
     */
    int createRechargeRecord(RechargeRecord rechargeRecord);

    /** 根据商户订单号查询充值记录
     * @param rechareNo 商户订单号
     * @return
     */
    RechargeRecord queryRechargeByRechargeNo(String rechareNo);
}
