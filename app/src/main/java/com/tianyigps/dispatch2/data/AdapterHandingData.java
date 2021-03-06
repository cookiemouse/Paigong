package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterHandingData {
    private String name, address, id, callName, callNumber;
    private long submitTime;
    private long time;
    private int online, lineless, orderType, checkStatus, orderStatus;
    private int removeWireNum, removeWirelessNum;
    private boolean modify;

    public AdapterHandingData(String name, long submitTime, long time, String address
            , String id, String callName, String callNumber
            , int online, int lineless) {
        this.name = name;
        this.submitTime = submitTime;
        this.time = time;
        this.address = address;
        this.id = id;
        this.callName = callName;
        this.callNumber = callNumber;
        this.online = online;
        this.lineless = lineless;
        this.modify = false;
    }

    public AdapterHandingData(String name, long submitTime, long time, String address
            , String id, String callName, String callNumber
            , int online, int lineless, boolean modify) {
        this.name = name;
        this.submitTime = submitTime;
        this.time = time;
        this.address = address;
        this.id = id;
        this.callName = callName;
        this.callNumber = callNumber;
        this.online = online;
        this.lineless = lineless;
        this.modify = modify;
    }

    public AdapterHandingData(String name, long submitTime, long time, String address, String id, String callName, String callNumber, int checkStatus
            , int orderType, int online, int lineless, boolean modify) {
        this.name = name;
        this.submitTime = submitTime;
        this.time = time;
        this.address = address;
        this.id = id;
        this.callName = callName;
        this.callNumber = callNumber;
        this.checkStatus = checkStatus;
        this.orderType = orderType;
        this.online = online;
        this.lineless = lineless;
        this.modify = modify;
    }

    public AdapterHandingData(String name, long submitTime, long time, String address, String id, String callName, String callNumber
            , int orderType, int checkStatus, int orderStatus, int online, int lineless, int removeWireNum, int removeWirelessNum, boolean modify) {
        this.name = name;
        this.submitTime = submitTime;
        this.time = time;
        this.address = address;
        this.id = id;
        this.callName = callName;
        this.callNumber = callNumber;
        this.online = online;
        this.lineless = lineless;
        this.orderType = orderType;
        this.checkStatus = checkStatus;
        this.orderStatus = orderStatus;
        this.removeWireNum = removeWireNum;
        this.removeWirelessNum = removeWirelessNum;
        this.modify = modify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getLineless() {
        return lineless;
    }

    public void setLineless(int lineless) {
        this.lineless = lineless;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getRemoveWireNum() {
        return removeWireNum;
    }

    public void setRemoveWireNum(int removeWireNum) {
        this.removeWireNum = removeWireNum;
    }

    public int getRemoveWirelessNum() {
        return removeWirelessNum;
    }

    public void setRemoveWirelessNum(int removeWirelessNum) {
        this.removeWirelessNum = removeWirelessNum;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }
}
