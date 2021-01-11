package com.bjpowernode.p2p.mapper.loan;

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.loan.RecentInvestment;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**查询首页年利率
     * @return int
     */
    BigDecimal selectYearRate();

    /** 首页按产品的类型分页查询
     * @param  productType,offSet,rows
     * @return
     */
    List<LoanInfo> selectLoanInfoForIndex(@Param("type") Integer productType,
                                          @Param("offSet") Integer offSet,
                                          @Param("rows") Integer rows);

    /** 更多产品按产品的类型分页查询
     * @param  productType 商品类型
     * @param  productType 页号
     * @return
     */
    List<LoanInfo> selectLoanInfoForLoan(@Param("type") Integer productType,
                                          @Param("offSet") Integer offSet);

    /**按产品的类型分页查询更多商品，总页数
     * @param ptype 商品类型
     * @return
     */
    int selectRowsByProductType(@Param("ptype") Integer ptype);

    /** 点立即投资查询商品的详细信息
     * @param id  商品主键id
     * @return loanInfo产品实体类
     */
    LoanInfo selectProductDetailsById(@Param("id") Integer id);

    /** 用户个人详情页面，查询最近投资
     * @param uId 用户表主键id
     * @return
     */
    List<RecentInvestment> selectRecentInvestment(Integer uId);

    /**
     * 使用乐观锁的更新
     */
    int updateLoanInfoById(Integer loanId,Double bidMoney,Integer version);

    /** 根据产品状态查询
     * @param status 产品状态（0未满标，1已满标，2满标已生成收益计划）
     * @return
     */
    List<LoanInfo> selectByProductStatus(@Param("status") Integer status);
}