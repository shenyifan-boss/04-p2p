package com.bjpowernode.common;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/27
 **/
public class Constants {
    //session中的key
    public static final String SESSION_USER="sessionUser";
    //历史利率
    public static final String HISTORY_AVG_RATE="historyAvgRate";
    //用户数
    public static final String USERS_COUNT="usersCount";
    //累计成交金额
    public static final String TRANSACTION_AMOUNT="TransactionAmount";
    //新手宝
    public static final Integer X_LOANINFO_PRODUCT_TYPE=0;
    //优选
    public static final Integer Y_LOANINFO_PRODUCT_TYPE=1;
    //散标
    public static final Integer S_LOANINFO_PRODUCT_TYPE=2;

    /**
     * 京东万象
     */
    //106短信的url
    public static final String JDWX_106SMS_URL="https://way.jd.com/kaixintong/kaixintong";
    //106短信的key
    public static final String JDWX_106SMS_APPKEY="54dfd6440b74bc283bf18cb693bf2098";
    //短信的模板
    public static final String JDWX_SMS_CONTENT="【凯信通】您的验证码是：%s";
    //身份验证的url
    public static final String JDWX_IDENTITY_URL="https://way.jd.com/youhuoBeijing/test";
    //身份验证的key
    public static final String JDWX_IDENTITY_APPKEY="54dfd6440b74bc283bf18cb693bf2098";

    /**
     * 产品类型
     */
    //满标
    public static final Integer LOAN_PRODUCTSTATUS_FULL=1;
    //未满标
    public static final Integer LOAN_PRODUCTSTATUS_NOT_FULL=0;
    //满标生成收益计划
    public static final Integer LOAN_PRODUCTSTATUS_INCOME_PLAN=2;


    /**
     *  redis
     */
    //发送短信验证码的key
    public static final String REDIS_KEY="SMS:CODE:";
    public static final Object INVEST_TOP = "INVERT:TOP";

    /**
     * 支付宝相关
     */
    public static final String ALIPAY_TRANDE_NO_NUMBER = "PAY:ALIPAY:TRADENO";

    /**
     * 充值状态
     */
    //状态
    public static final String RECHAGE_START = "0";
    public static final String RECHAGE_SUCCESS = "1";
    public static final String RECHAGE_FAIL = "2";
}
