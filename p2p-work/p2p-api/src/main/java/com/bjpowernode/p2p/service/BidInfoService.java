package com.bjpowernode.p2p.service;

import com.bjpowernode.common.ErrorEnum;
import com.bjpowernode.vo.InvestmentRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * 奕凡
 * 2020/12/28
 **/
public interface BidInfoService {
    /** 详情页面根据投资记录表id查询投资记录
     * @param id  投资记录表主键
     * @return 根据页面所需自定义投资记录实体类的list
     */
    List<InvestmentRecord> queryInvestmentRecordById(Integer id);

    /**
     *  投资
     * @param userId 用户表主键
     * @param loanId 产品id
     * @param bigDecimal 投资多少钱
     * @return
     */
    ErrorEnum invest(Integer userId, Integer loanId, Double bigDecimal);
}
