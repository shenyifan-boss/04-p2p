package com.bjpowernode.vo;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/4
 **/
//投资排行榜
public class InvestmentRanking {
    private String phone;
    private String money;

    public InvestmentRanking(String phone, String money) {
        this.phone = phone;
        this.money = money;
    }

    public InvestmentRanking() {
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
}
