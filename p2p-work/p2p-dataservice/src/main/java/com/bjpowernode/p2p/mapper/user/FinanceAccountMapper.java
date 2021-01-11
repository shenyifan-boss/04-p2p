package com.bjpowernode.p2p.mapper.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /** 查询用户剩余金额
     * @param uid user的主键
     * @return
     */
    FinanceAccount selectMoneyForLogin(@Param("uid") Integer uid);

    /**
     * 给一行记录上锁
     */
    FinanceAccount selectByUserIdForUpdate(@Param("userId") Integer userId);

    /**
     * 更新账号的金额
     *
     */
    int updateFinaceAccount(Integer userId,Double account);

    /**
     * 收益到期返回给资金账号
     * @param userId
     * @param bidMoney 投资金额
     * @param incomeMoney 收益金额
     * @return
     */
    int updateFinanceAccountIncomeBack(@Param("userId") Integer userId,
                                       @Param("bidMoney") BigDecimal bidMoney,
                                       @Param("incomeMoney") BigDecimal incomeMoney);
}