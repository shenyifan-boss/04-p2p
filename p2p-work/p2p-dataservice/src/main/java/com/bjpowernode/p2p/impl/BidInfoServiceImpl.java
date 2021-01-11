package com.bjpowernode.p2p.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.common.Constants;
import com.bjpowernode.common.ErrorEnum;
import com.bjpowernode.excep.loan.CreateBidInfoException;
import com.bjpowernode.excep.loan.UpdateFinaceAccountMoneyException;
import com.bjpowernode.excep.loan.UpdateLeftProductMoney;
import com.bjpowernode.excep.loan.UpdateLoanInfoException;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.service.BidInfoService;
import com.bjpowernode.vo.InvestmentRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/28
 **/
@Component
@Service(interfaceClass = BidInfoService.class,version = "1.0",timeout = 3500)
public class BidInfoServiceImpl implements BidInfoService {
    /** 详情页面根据投资记录表id查询投资记录
     * @param id  投资记录表主键
     * @return 根据页面所需自定义投资记录实体类的list
     */
    @Resource
    private BidInfoMapper bidInfoMapper;

    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Resource
    private LoanInfoMapper loanInfoMapper;

    @Override
    public List<InvestmentRecord> queryInvestmentRecordById(Integer id){
        List<InvestmentRecord> investmentRecords = bidInfoMapper.selectInvestmentRecordById(id);
        return investmentRecords;
    }

    /**
     *  投资
     * @param userId 用户表主键
     * @param loanId 产品id
     * @param bigDecimal 投资多少钱
     * @return
     */

    @Override
    @Transactional
    public ErrorEnum invest(Integer userId, Integer loanId, Double bigDecimal) {
        ErrorEnum result = ErrorEnum.DEFAULT_UNKNOW;
        int row = 0;
        //1.查询产品
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        //2.判断是否满标
        if(loanInfo!=null){
            if(loanInfo.getProductStatus()== Constants.LOAN_PRODUCTSTATUS_NOT_FULL){
                //判断金额是否符合购买要求
                if (bigDecimal.compareTo(loanInfo.getBidMinLimit()) > -1 && bigDecimal.compareTo(loanInfo.getBidMaxLimit()) < 1) {
                    //金额正确的
                    //3.查询用户的账号金额
                    //上锁 for update语句
                    FinanceAccount account = financeAccountMapper.selectByUserIdForUpdate(userId);
                    Double availableMoney = account.getAvailableMoney();
                    if(bigDecimal<=availableMoney ){
                        //金额够可投资
                        //4.扣除金额
                         row = financeAccountMapper.updateFinaceAccount(userId, bigDecimal);
                        if(row<1){
                            //抛异常回滚
                            throw new UpdateFinaceAccountMoneyException("投资更新资金账号余额失败");
                        }

                        //5.扣除产品的剩余可投资金额，使用乐观锁
                        row = loanInfoMapper.updateLoanInfoById(loanInfo.getId(), bigDecimal, loanInfo.getVersion());
                        if(row<1){
                            //抛异常
                            throw new UpdateLeftProductMoney("更新产品的剩余可投资金额失败");
                        }
                        //6.生成投资记录
                        BidInfo bidInfo =new BidInfo();
                        bidInfo.setUid(userId);
                        bidInfo.setLoanId(loanId);
                        bidInfo.setBidMoney(bigDecimal);
                        bidInfo.setBidTime(new Date());
                        bidInfo.setBidStatus(1);//开始投资
                        row = bidInfoMapper.insertSelective(bidInfo);
                        if(row<1){
                            throw new CreateBidInfoException("生成投资记录失败");
                        }
                        //7.查询产品
                        LoanInfo reloanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
                        //8.判断是否满标
                        if(reloanInfo.getLeftProductMoney() == 0){
                            //9.更新状态为满标
                            reloanInfo.setProductStatus(Constants.LOAN_PRODUCTSTATUS_FULL);
                            reloanInfo.setProductFullTime(new Date());
                            row = loanInfoMapper.updateByPrimaryKeySelective(reloanInfo);
                            if(row<1){
                                //抛异常
                                throw new UpdateLoanInfoException("更新产品满标状态错误");
                            }
                        }
                        //成功
                        result=ErrorEnum.success;
                    }else {
                        //用户月不足
                        result=ErrorEnum.BIDMONEY_NOT_ENOUGH;
                    }
                }else {
                    //金额不符合要求
                    result=ErrorEnum.BIDMONEY_NOT_RANGE;
                }
            }else {
                //产品不能购买
                result=ErrorEnum.LOAN_NOT_INVEST;
            }
        } else {
            //产品不存在
            result=ErrorEnum.LOAN_NOT_EXISTS;
        }
        return result;
    }
}
