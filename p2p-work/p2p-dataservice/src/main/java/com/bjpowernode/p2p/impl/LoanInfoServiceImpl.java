package com.bjpowernode.p2p.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.common.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.loan.RecentInvestment;
import com.bjpowernode.p2p.service.LoanInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/25
 **/
//创建对象
@Component
//作为提供者要暴露服务so用service(dubbo的service)
@Service(interfaceClass = LoanInfoService.class,version = "1.0",timeout = 3500)
public class LoanInfoServiceImpl implements LoanInfoService {

    @Resource
    private LoanInfoMapper loanInfoMapper;
    @Resource
    private RedisTemplate redisTemplate;
    /**计算产品历史年化率
     * @return  BigDecimal
     */
    @Override
    public BigDecimal queryRate() {
        BigDecimal historyAvgRate = (BigDecimal)redisTemplate.opsForValue().get(Constants.HISTORY_AVG_RATE);
        if(historyAvgRate==null){
            synchronized (this){
                //确保数据是null
                historyAvgRate = (BigDecimal)redisTemplate.opsForValue().get(Constants.HISTORY_AVG_RATE);
                if(historyAvgRate==null){
                    historyAvgRate = loanInfoMapper.selectYearRate();
                    if(ObjectUtils.allNotNull(historyAvgRate)){
                        redisTemplate.opsForValue().set("historyAvgRate", historyAvgRate,20, TimeUnit.MINUTES);
                    }
                }
            }
        }
        return historyAvgRate;
    }
    /** 按产品的类型分页查询
     * @param  productType 产品类型
     * @param  pageNo       页号
     * @param  pageSize     每页多少条数据
     * @return
     */
    @Override
    public List<LoanInfo> queryLoanInfoForIndex(Integer productType, Integer pageNo, Integer pageSize) {
        List<LoanInfo> loanInfoList = null;
        if(productType==null){
            productType=Constants.X_LOANINFO_PRODUCT_TYPE;
        }
            pageNo= (pageNo==null)?1:pageNo;
            pageSize=(pageSize==null || pageSize>51)?10:pageSize;
            int offSet=(pageNo-1)*pageSize;
            loanInfoList= loanInfoMapper.selectLoanInfoForIndex(productType,offSet, pageSize);


        return loanInfoList;
    }
    /** 更多商品按产品的类型分页查询
     * @param  productType 产品类型
     * @param  pageNo       页号
     * @return
     */
    @Override
    public List<LoanInfo> queryLoanInfoForLoan(Integer productType, Integer pageNo) {
        List<LoanInfo> loanInfoList = null;
        if(productType==null){
            productType=Constants.X_LOANINFO_PRODUCT_TYPE;
        }
        pageNo= (pageNo==null)?1:pageNo;
        int offSet=(pageNo-1)*9;
        loanInfoList= loanInfoMapper.selectLoanInfoForLoan(productType, offSet);

        return loanInfoList;
    }
    /** 按产品的类型分页查询更多商品，总页数
     * @param
     * @return
     */
    @Override
    public int countRowsByProductType(Integer ptype) {
        int row = loanInfoMapper.selectRowsByProductType(ptype);
        return row;
    }
    /** 点立即投资查询商品的详细信息
     * @param id  商品主键id
     * @return loanInfo产品实体类
     */
    @Override
    public LoanInfo queryProductDetailsById(Integer id) {
        if(ObjectUtils.isEmpty(id)){
            id=1310699;
        }
        LoanInfo loanInfo = loanInfoMapper.selectProductDetailsById(id);
        return loanInfo;
    }
    /** 用户个人详情页面，查询最近投资
     * @param uId 用户主键id
     * @return
     */
    @Override
    public List<RecentInvestment> queryRecentInvestment(Integer uId) {

        return loanInfoMapper.selectRecentInvestment(uId);
    }
}
