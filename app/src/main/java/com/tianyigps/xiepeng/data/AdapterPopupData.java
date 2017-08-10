package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class AdapterPopupData {
    private int count;

    private int orderStatus;

    public AdapterPopupData(int orderStatus, int count) {
        this.count = count;
        this.orderStatus = orderStatus;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
