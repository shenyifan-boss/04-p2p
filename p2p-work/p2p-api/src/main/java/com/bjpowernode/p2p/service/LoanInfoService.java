package com.bjpowernode.p2p.service;

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.loan.RecentInvestment;

import java.math.BigDecimal;
import java.util.List;

/**
 * 奕凡
 * 2020/12/25
 **/

public interface LoanInfoService {
    /**计算产品历史年化率
     * @return  BigDecimal
     */
    BigDecimal queryRate();

    /** 按产品的类型分页查询
     * @param
     * @return
     */
    List<LoanInfo> queryLoanInfoForIndex(Integer productType,
                                         Integer pageNo,
                                         Integer pageSize);

    /** 按产品的类型分页查询更多产品
     * @param
     * @return
     */
    List<LoanInfo> queryLoanInfoForLoan(Integer productType,
                                         Integer pageNo);
    /** 按产品的类型分页查询更多商品，总页数
     * @param
     * @return
     */
    int countRowsByProductType(Integer ptype);

    /** 点立即投资查询商品的详细信息
     * @param id  商品主键id
     * @return loanInfo产品实体类
     */
    LoanInfo queryProductDetailsById(Integer id);

    /** 用户个人详情页面，查询最近投资
     * @param uId 用户表主键id
     * @return
     */
    List<RecentInvestment> queryRecentInvestment(Integer uId);
}
