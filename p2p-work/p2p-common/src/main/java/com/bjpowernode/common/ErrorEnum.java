package com.bjpowernode.common;

/**
 * 奕凡
 * 2020/12/28
 **/
public enum ErrorEnum {

    PHONE_FORMART_ERROR(1001,"手机号格式错误"),
    PHONE_EXISTS(1002,"手机号已经存在"),
    PHONE_IS_NULL(1003,"手机号为空"),
    PASSWORD_FORMAT_ERROR(1004,"密码不符合要求"),
    PEGISTER_SMS_CODE_ERROR(1005,"验证码不正确"),
    LOAN_NOT_INVEST(1007,"不能购买"),
    LOAN_NOT_EXISTS(1008,"产品不存在"),
    BIDMONEY_NOT_RANGE(1009,"投资金额不允许"),
    BIDMONEY_NOT_ENOUGH(1010,"用户余额不足"),
    DEFAULT_UNKNOW(9999,"未知"),
    success(1000,"成功");

    private Integer code;
    private String msg;


    private ErrorEnum(Integer code,String msg){
        this.code=code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }}
