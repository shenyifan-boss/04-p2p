package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.common.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.loan.RecentInvestment;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.BidInfoService;
import com.bjpowernode.p2p.service.FinanceAccountService;
import com.bjpowernode.p2p.service.LoanInfoService;
import com.bjpowernode.vo.InvestmentRanking;
import com.bjpowernode.vo.InvestmentRecord;
import com.bjpowernode.vo.page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/27
 **/
@Controller
public class LoanInfoController {
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0")
    private LoanInfoService loanInfoService;
    @Reference(interfaceClass = BidInfoService.class,version = "1.0")
    private BidInfoService bidInfoService;
    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0")
    private FinanceAccountService financeAccountService;
    @Resource
    private RedisTemplate redisTemplate;
    @GetMapping("/loan/loan")
    public String loan(@RequestParam("ptype") Integer ptype,
                       @RequestParam(value = "pageNo",required = false) Integer pageNo,
                       Model model){
        pageNo= pageNo==null? 1 : pageNo;
        List<InvestmentRanking> list = new ArrayList<>();
        List<LoanInfo> loanInfoList = loanInfoService.queryLoanInfoForLoan(ptype,pageNo);
        model.addAttribute("loanInfoList",loanInfoList);
        //按产品类型计算总计算数
        int totalRows = loanInfoService.countRowsByProductType(ptype);
        //需要分页的对象int pageSize=9;
        page page =new page(pageNo, 9, totalRows);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 5);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = set.iterator();
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<String> next = iterator.next();
            InvestmentRanking ranking =new InvestmentRanking(next.getValue(), String.valueOf(next.getScore()));
            list.add(ranking);
        }
        model.addAttribute("type",ptype);

        model.addAttribute("page",page);
        //取数据，更新投资排行榜
        model.addAttribute("list",list);
        return "loan";
    }
    @GetMapping("/loan/loanInfo")
    public String loanInfo(@RequestParam(value = "id",required = false) Integer id, Model model, HttpSession session){
        LoanInfo loanInfo = loanInfoService.queryProductDetailsById(id);
        List<InvestmentRecord> investmentList = bidInfoService.queryInvestmentRecordById(id);
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        FinanceAccount account = financeAccountService.queryMoneyForLogin(user.getId());
        model.addAttribute("account",account);
        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("investmentList",investmentList);
        return "loanInfo";
    }
}
