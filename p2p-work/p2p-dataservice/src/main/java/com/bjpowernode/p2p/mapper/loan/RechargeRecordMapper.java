package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.RechargeRecord;
import org.apache.ibatis.annotations.Param;

public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    /** 根据商户订单号查询充值记录
     * @param rechareNo 商户订单号
     * @return
     */
    RechargeRecord queryRechargeByRechargeNo(@Param("rechareNo") String rechareNo);
}