package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.vo.InvestmentRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);


    /** 详情页面根据投资记录表id查询投资记录
     * @param id  投资记录表主键
     * @return 根据页面所需自定义投资记录实体类的list
     */
    List<InvestmentRecord> selectInvestmentRecordById(@Param("id") Integer id);

    /** 根据产品id查询投资记录
     * @param loanId 产品id
     * @return
     */
    List<BidInfo> selectByLoanId(@Param("loanId") Integer loanId);
}