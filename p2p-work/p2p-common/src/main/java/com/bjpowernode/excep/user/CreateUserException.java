package com.bjpowernode.excep.user;

import com.bjpowernode.excep.P2pCustomException;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/29
 **/
public class CreateUserException extends P2pCustomException {
    public CreateUserException() {
        super();
    }

    public CreateUserException(String message) {
        super(message);
    }
}
