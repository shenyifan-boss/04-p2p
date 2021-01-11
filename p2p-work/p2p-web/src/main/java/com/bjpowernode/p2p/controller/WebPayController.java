package com.bjpowernode.p2p.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/7
 **/
@Controller
public class WebPayController {
    @Value("${pay.alipay.url}")
    private String alipayUrl ;
    //进入充值支付页面
    @RequestMapping("/loan/page/toRecharge")
    public String pageRecharge(Model model){
        model.addAttribute("alipayUrl",alipayUrl);
//        model.addAttribute("weixinpayUrl","/loan/weixinpay");
        return "toRecharge";
    }

    //支付宝充值
    /*@RequestMapping("/loan/alipay")
    public String pageAliPay(){

        return "";
    }*/
    //微信充值
    /*@RequestMapping("/loan/weixinpay")
    public String pageWeixinPay(){
        return "";
    }*/
}
