package com.bjpowernode.excep.loan;

import com.bjpowernode.excep.P2pCustomException;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/3
 **/
public class UpdateLeftProductMoney extends P2pCustomException {
    public UpdateLeftProductMoney() {
        super();
    }

    public UpdateLeftProductMoney(String message) {
        super(message);
    }
}
