package com.bjpowernode.p2p.model.loan;

import java.io.Serializable;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/3
 **/
//最近投资实体类
public class RecentInvestment  implements Serializable {

    private String productName;
    private Integer money;
    private String bidTime;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }
}
