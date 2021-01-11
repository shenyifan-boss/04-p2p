package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.user.FinanceAccount;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/2
 **/
public interface FinanceAccountService {

    /** 查询用户剩余金额
     * @param uid user的主键
     * @return
     */
    FinanceAccount queryMoneyForLogin(Integer uid);
}
