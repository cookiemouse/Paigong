package com.tianyigps.dispatch2.bean;

import com.tianyigps.dispatch2.utils.RegularU;

/**
 * Created by cookie on 17-8-15.
 */

public class PersonBean {
    /**
     * person : 2
     */

    private String person;
    private String orderId4New;
    private String orderNo;

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getOrderId4New() {
        return orderId4New;
    }

    public void setOrderId4New(String orderId4New) {
        this.orderId4New = orderId4New;
    }

    public String getOrderNo() {
        if (RegularU.isEmpty(orderNo)) {
            return "";
        }
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
