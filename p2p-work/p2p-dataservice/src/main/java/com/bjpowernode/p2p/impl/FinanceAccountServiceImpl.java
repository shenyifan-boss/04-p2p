package com.bjpowernode.p2p.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.service.FinanceAccountService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/2
 **/
@Component
@Service(interfaceClass = FinanceAccountService.class,version = "1.0",timeout = 3500)
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    /** 查询用户剩余金额
     * @param uid user的主键
     * @return
     */
    @Override
    public FinanceAccount queryMoneyForLogin(Integer uid) {
        return financeAccountMapper.selectMoneyForLogin(uid);
    }
}
