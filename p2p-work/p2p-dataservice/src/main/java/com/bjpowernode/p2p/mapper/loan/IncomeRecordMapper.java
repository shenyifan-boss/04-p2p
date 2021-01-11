package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.IncomeRecord;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    /** 查询累计成交金额为主页
     * @return BigDecimal
     */
    BigDecimal selectMoneyForIndex();

    /** 到期的记录
     * @return
     */
    List<IncomeRecord> selectExpirePecord();

}