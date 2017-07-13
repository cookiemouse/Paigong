package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterOrderData {
    private String id, name, phoneNumber, phoneName, time, address, orderType;
    int lineNumber, linelessNumber;

    public AdapterOrderData(String name, String time, String address
            , String id, String phoneName, String phoneNumber, String orderType
            , int lineNumber, int linelessNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.phoneName = phoneName;
        this.time = time;
        this.address = address;
        this.orderType = orderType;
        this.lineNumber = lineNumber;
        this.linelessNumber = linelessNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
