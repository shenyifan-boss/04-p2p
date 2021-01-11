package com.bjpowernode.excep;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/29
 **/
//自定义异常父类
public class P2pCustomException extends RuntimeException {
    public P2pCustomException() {
    }

    public P2pCustomException(String message) {
        super(message);
    }
}
