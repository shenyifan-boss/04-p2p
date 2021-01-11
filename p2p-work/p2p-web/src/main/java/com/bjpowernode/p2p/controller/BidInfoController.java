package com.bjpowernode.p2p.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.common.Constants;
import com.bjpowernode.common.ErrorEnum;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.BidInfoService;
import com.bjpowernode.vo.CommonResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/3
 **/
@Controller
public class BidInfoController {
    @Reference(interfaceClass = BidInfoService.class,version = "1.0",check = false)
    private BidInfoService bidInfoService;
    @Resource
    private RedisTemplate redisTemplate;
    @PostMapping("/bid/investRequest")
    @ResponseBody
    public CommonResult investRequest(HttpSession session , Double bidmoney , Integer loanId){
        CommonResult result = new CommonResult().faled("客服端错误", "");
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        ErrorEnum errorEnum =bidInfoService.invest(user.getId(), loanId, bidmoney);
        if(errorEnum==ErrorEnum.success){
            //投资成功
            result = new CommonResult().success("投资成功", "");
            //更新投资排行榜
            redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP, user.getPhone(), bidmoney);
        }
        return result;
    }
}
