package com.example.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.bjpowernode.common.Constants;
import com.bjpowernode.p2p.model.loan.RecentInvestment;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.service.ReChargeService;
import com.example.config.AlipayConfig;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/7
 **/
@Service
public class AlipayService {
    @Reference(interfaceClass = ReChargeService.class,version = "1.0",check = false)
    private ReChargeService reChargeService;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private AlipayClient alipayClient;
    /** 下单 统一收单
     * @param totalAmount 金额
     * @return
     */
    public String pagePay(Integer uid,String totalAmount){
        //数据库操作开始
        //生成订单记录
        int row = -1;
        String result = null;
        String p2pTradeNo = p2pTradeNo();
        RechargeRecord reCharge =new RechargeRecord();
        reCharge.setUid(uid);
        reCharge.setRechargeDesc("使用支付宝充值");
        reCharge.setRechargeNo(p2pTradeNo);
        reCharge.setRechargeStatus(Constants.RECHAGE_START);
        reCharge.setRechargeTime(new Date());
        reCharge.setChannel("alipay");

        reCharge.setRechargeMoney(Double.valueOf(totalAmount));
        row = reChargeService.createRechargeRecord(reCharge);
        if (row>0){
            //数据库操作结束

            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
            /**
             * 商户订单号 ： 使用时间 yyyyMMddHHmmss + redis生成的唯一值
             */
            //商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = p2pTradeNo;
            //付款金额，必填
            String total_amount = totalAmount;
            //订单名称，必填
            String subject = "s5";
            //商品描述，可空
            String body = "";

            alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                    + "\"total_amount\":\""+ total_amount +"\","
                    + "\"subject\":\""+ subject +"\","
                    + "\"body\":\""+ body +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

            //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
            //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
            //		+ "\"total_amount\":\""+ total_amount +"\","
            //		+ "\"subject\":\""+ subject +"\","
            //		+ "\"body\":\""+ body +"\","
            //		+ "\"timeout_express\":\"10m\","
            //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

            //请求

            try {
                result = alipayClient.pageExecute(alipayRequest).getBody();
            } catch (AlipayApiException e) {
                result=null;
                e.printStackTrace();
            }
        }

        //输出
        return result;
    }

    /**
     * 异步通知业务处理
     */
    public void alipayNotify(Map<String,String> params) throws AlipayApiException {
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——

	 /*实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。*/

        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = params.get("out_trade_no");

            RechargeRecord rechargeRecord = reChargeService.queryRechargeByRechargeNo(out_trade_no);
            //支付宝交易号
            String trade_no = params.get("total_amount");

            //交易状态
            String trade_status = params.get("trade_status");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }


        }//验证失败


            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);


    }

    /** 生成商户订单号
     * @return
     */
    private String p2pTradeNo(){

        String nowDate = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        //和数据库自增型一样，increment
        Long num = redisTemplate.opsForValue().increment(Constants.ALIPAY_TRANDE_NO_NUMBER);
        return nowDate+num;
    }
}
