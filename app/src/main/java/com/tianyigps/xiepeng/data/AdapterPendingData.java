package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/11.
 */

public class AdapterPendingData {

    private String order, name, contactName, phoneNumber, time, address, orderType;
    int lineNumber, linelessNumber;

    int orderId;

    public AdapterPendingData(String order, String name, String contactName, String phoneNumber
            , String time, String address, String orderType
            , int lineNumber, int linelessNumber, int orderId) {
        this.order = order;
        this.name = name;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.time = time;
        this.address = address;
        this.orderType = orderType;
        this.lineNumber = lineNumber;
        this.linelessNumber = linelessNumber;
        this.orderId = orderId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLinelessNumber() {
        return linelessNumber;
    }

    public void setLinelessNumber(int linelessNumber) {
        this.linelessNumber = linelessNumber;
    }
}
