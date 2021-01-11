package com.bjpowernode.p2p.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.service.ReChargeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/7
 **/
@Component
@Service(interfaceClass = ReChargeService.class,version = "1.0",timeout = 3500)
public class ReChargeServiceImpl implements ReChargeService {
    @Resource
    private RechargeRecordMapper rechargeRecordMapper;
    /** 创建充值记录
     * @param rechargeRecord
     * @return
     */

    @Override
    public int createRechargeRecord(RechargeRecord rechargeRecord) {
        int row = rechargeRecordMapper.insertSelective(rechargeRecord);
        return row;
    }

    /** 根据商户订单号查询充值记录
     * @param rechareNo 商户订单号
     * @return
     */
    @Override
    public RechargeRecord queryRechargeByRechargeNo(String rechareNo) {
        return rechargeRecordMapper.queryRechargeByRechargeNo(rechareNo);
    }
}
