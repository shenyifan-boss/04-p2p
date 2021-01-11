package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.common.Constants;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.FinanceAccountService;
import com.bjpowernode.vo.CommonResult;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/2
 **/
@Component
public class FinanceAccountController {
    /*@Reference(interfaceClass = FinanceAccountService.class,version = "1.0" , timeout = 3500)
    private FinanceAccountService financeAccountService;

    @RequestMapping("/loan/queryAccount")
    @ResponseBody
    public CommonResult queryMoney(HttpSession session){
        CommonResult result =new CommonResult();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if(ObjectUtils.isEmpty(user)){
            result=null;
        }else if(!ObjectUtils.isEmpty(user)){
            FinanceAccount account = financeAccountService.queryMoneyForLogin(user.getId());
            result=new CommonResult().success("成功", account.getAvailableMoney());
        }
        return result;
    }*/
}
