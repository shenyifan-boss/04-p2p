package com.bjpowernode.vo;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/28
 **/
//表示ajax请求结果
public class CommonResult {

    /**
     * 表示忘了通信结果，等同于httpStatus
     *  200成功
     *  300
     *  400 客服端错误
     *  500服务器端错误
     *
     */
    private Integer code;

    /**
     *  错误提示
     *  1000 ：无错误
     *  9999 ：未知错误
     *  1001 ：手机号格式有误
     *  1002 ：手机号已经注册过
     *  1003 : 手机号为空
     *  1004 : 参数为空
     */
    private Integer error;

    /**
     *  错误描述
     */
    private String message;

    /**
     *  数据
     */
    private Object data;

    public CommonResult(Integer code, Integer error, String message, Object data) {
        this.code = code;
        this.error = error;
        this.message = message;
        this.data = data;
    }

    /** 成功带数据的构造
     *
     */
    public CommonResult success(String message,Object data) {
        this.code = 200;
        this.error=9999;
        this.message=message;
        this.data=data;

        return this;
    }

    /** 失败的
     *
     */
    public CommonResult faled(String message,Object data) {
        this.code = 400;
        this.error=1000;
        this.message=message;
        this.data=data;

        return this;
    }

    public CommonResult() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
