package com.bjpowernode.vo;

import java.io.Serializable;

/**
 * 奕凡
 * <p>
 * <p>
 * 2020/12/28
 **/
public class InvestmentRecord implements Serializable {
    private String phone;
    private String money;
    private String time;

    public InvestmentRecord(String phone, String money, String time) {
        this.phone = phone;
        this.money = money;
        this.time = time;
    }

    public InvestmentRecord() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
