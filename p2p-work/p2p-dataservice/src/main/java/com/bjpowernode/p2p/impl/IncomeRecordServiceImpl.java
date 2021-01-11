package com.bjpowernode.p2p.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.common.Constants;
import com.bjpowernode.excep.loan.InsertIncomeRecordException;
import com.bjpowernode.excep.loan.UpdateFinaceAccountMoneyException;
import com.bjpowernode.excep.loan.UpdateIncomeRecordStautsException;
import com.bjpowernode.excep.loan.UpdateLoanInfoException;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.service.IncomeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.ibatis.ognl.OgnlOps.doubleValue;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/27
 **/
@Slf4j
@Component
@Transactional
@Service(interfaceClass = IncomeRecordService.class,version = "1.0",timeout = 88883500)
public class IncomeRecordServiceImpl implements IncomeRecordService {
    @Resource
    private IncomeRecordMapper incomeRecordMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private LoanInfoMapper loanInfoMapper;
    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    private Object lockOBj = new Object();

    @Override
    public BigDecimal selectMoneyForIndex() {
        BigDecimal TransactionAmount = (BigDecimal)redisTemplate.opsForValue().get(Constants.TRANSACTION_AMOUNT);
        if(TransactionAmount==null){
            synchronized (this){
                TransactionAmount = (BigDecimal)redisTemplate.opsForValue().get(Constants.TRANSACTION_AMOUNT);
                if(TransactionAmount==null){
                    TransactionAmount = incomeRecordMapper.selectMoneyForIndex();
                    if(TransactionAmount!=null){
                        redisTemplate.opsForValue().set("TransactionAmount", TransactionAmount,20, TimeUnit.MINUTES);
                    }
                }
            }
        }
        return incomeRecordMapper.selectMoneyForIndex();
    }
    /**
     * 生成收益计划
     */
    @Override
    @Transactional
    public synchronized void generateIncomePan() {
        log.debug("生成收益计划开始");
        //1.查询满标产品
        //产品类型
        Integer productType=-1;
        //总收益
        BigDecimal incomeMoney = new BigDecimal("0");
        //投资金额
        BigDecimal bigMoney = new BigDecimal("0");
        //投资周期
        Integer cycle = 0;
        //利率
        BigDecimal rate = null;
        //收益时间
        Date incomeDate = null;
        //满标时间
        Date fullDate = null;
        int row = 0;
        List<LoanInfo> loanInfoList = loanInfoMapper.selectByProductStatus(Constants.LOAN_PRODUCTSTATUS_FULL);


        for (LoanInfo loanInfo : loanInfoList) {
            log.debug("生成收益计划开始| 产品id="+loanInfo.getId());
            fullDate = loanInfo.getProductFullTime();
            rate = BigDecimal.valueOf(loanInfo.getRate());
            cycle = loanInfo.getCycle();
            productType = loanInfo.getProductType();
            //2.获取每个产品的投资记录
            List<BidInfo> bidInfos = bidInfoMapper.selectByLoanId(loanInfo.getId());
            for (BidInfo bidInfo : bidInfos) {
                log.debug("生成收益计划开始| 产品id="+loanInfo.getId()+"|获取每个产品投资记录 | bidInfo="+bidInfo.getId());
                //获取投资金额
                bigMoney =BigDecimal.valueOf(bidInfo.getBidMoney());
                //3.每个投资记录，生成收益计划（incomeRecord）
                IncomeRecord record =new IncomeRecord();
                record.setUid(bidInfo.getUid());
                record.setLoanId(bidInfo.getLoanId());
                record.setBidId(bidInfo.getId());
                record.setIncomeStatus(0);//生成收益计划未返还
                record.setBidMoney(bidInfo.getBidMoney());
                //计算时间和收益金额
                if(productType == Constants.X_LOANINFO_PRODUCT_TYPE){
                    //新手宝，周期是天
                    //利率（日）*投资周期*投资金额
                    incomeMoney = rate.divide(new BigDecimal("100"))
                            .divide(new BigDecimal("365"))
                            .multiply(bigMoney).multiply(new BigDecimal(cycle));
                    //收益时间
                    //产品的满标时间+周期
                    incomeDate = DateUtils.addDays(fullDate, cycle);

                }else {
                    //其他周期是月(30天)
                    incomeMoney = rate
                            .divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP)
                            .divide(new BigDecimal("365"),6,BigDecimal.ROUND_HALF_UP)
                            .multiply(bigMoney).multiply(new BigDecimal(cycle))
                            .multiply(new BigDecimal("30"));
                    incomeDate = DateUtils.addMonths(fullDate, cycle);
                }
                record.setIncomeDate(incomeDate);
                record.setIncomeMoney(doubleValue(incomeMoney));
                //添加记录
                row = incomeRecordMapper.insertSelective(record);
                if (row<0){
                    log.debug("生成收益计划开始| 产品id="+loanInfo.getId()+
                            "|获取每个产品投资记录 | bidInfo="+bidInfo.getId()+
                            "|添加记录"+bidInfo.getId());
                    throw new InsertIncomeRecordException("添加收益记录失败");
                }
            }
            //4.更新产品的状态 2 （已生成收益计划）
            loanInfo.setProductStatus(Constants.LOAN_PRODUCTSTATUS_INCOME_PLAN);
            row  = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
            if(row<1){
                log.debug("生成收益计划开始| 产品id="+loanInfo.getId()+
                        "|获取每个产品投资记录 | 更新状态失败");
                throw new UpdateLoanInfoException("更新产品状态失败");
            }
        }
    }

    /**
     * 收益返还
     */
    @Override
    @Transactional
    public void generateIncomeBack() {
        synchronized (lockOBj){
            int row = 0;
            //获取到期的收益计划的记录
            List<IncomeRecord> incomeRecords = incomeRecordMapper.selectExpirePecord();
            log.debug("获取收益计划开始");
            for (IncomeRecord incomeRecord : incomeRecords) {
                //更新 financeAccount 收益表
                log.debug("获取收益计划开始 | 更新开始 ");
                FinanceAccount account = financeAccountMapper.selectByUserIdForUpdate(incomeRecord.getUid());
                if(account!=null){
                   row =  financeAccountMapper.updateFinanceAccountIncomeBack(incomeRecord.getUid(), BigDecimal.valueOf(incomeRecord.getBidMoney()),BigDecimal.valueOf(incomeRecord.getIncomeMoney()));
                    if(row<1){
                        throw new UpdateFinaceAccountMoneyException("更新收益表失败");
                    }
                }
                //更新收益的状态为已返还 为1
                row  = incomeRecordMapper.updateByPrimaryKeySelective(incomeRecord);
                if(row<1){
                    throw new UpdateIncomeRecordStautsException("更新收益状态失败");
                }
            }
        }
    }
}
