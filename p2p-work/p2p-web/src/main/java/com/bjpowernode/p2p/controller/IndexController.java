package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.common.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.IncomeRecordService;
import com.bjpowernode.p2p.service.LoanInfoService;
import com.bjpowernode.p2p.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/25
 **/
@Controller
public class IndexController  {
    //加入日志门面
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0",timeout = 3500)
    private LoanInfoService loanInfoService;
    @Reference(interfaceClass = UserService.class,version = "1.0",timeout = 3500)
    private UserService userService;
    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0",timeout = 3500)
    private IncomeRecordService IncomeRecordService;
    @RequestMapping({"/index","/"})
    public String index(Model model){
        //历史年化利率
        BigDecimal rate = loanInfoService.queryRate();
        //平台用户数
        BigDecimal row = userService.queryUserCountForIndex();
        logger.debug("row="+row);
        //累计成交金额
        BigDecimal usersCount = IncomeRecordService.selectMoneyForIndex();
        model.addAttribute("row",row);
        model.addAttribute("rate",rate);
        model.addAttribute("usersCount",usersCount);

        //获取新手宝产品
        List<LoanInfo> Xlist = loanInfoService.queryLoanInfoForIndex(Constants.X_LOANINFO_PRODUCT_TYPE, 1, 1);
        model.addAttribute("Xlist",Xlist);
        //获取优选产品
        List<LoanInfo> Ylist = loanInfoService.queryLoanInfoForIndex(Constants.Y_LOANINFO_PRODUCT_TYPE, 1, 4);
        model.addAttribute("Ylist",Ylist);
        //获取散标产品
        List<LoanInfo> Slist = loanInfoService.queryLoanInfoForIndex(Constants.S_LOANINFO_PRODUCT_TYPE, 1, 8);
        model.addAttribute("Slist",Slist);
        return "index";
    }
}
