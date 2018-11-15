package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterOrderData {
    private String id, name, phoneNumber, phoneName, address;
    private long time;
    private int orderType;
    private int lineNumber, linelessNumber;
    private int wireRemove, wirelessRemove;
    private int reviseFlag;
    private boolean showNew;
    private boolean showModify;
    private int orderId;

    public AdapterOrderData(String name, long time, String address
            , String id, String phoneName, String phoneNumber, int orderType
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

    public AdapterOrderData(String id, String name, String phoneNumber, String phoneName, long time, String address, int orderType
            , int lineNumber, int linelessNumber, int wireRemove, int wirelessRemove, int reviseFlag) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.phoneName = phoneName;
        this.time = time;
        this.address = address;
        this.orderType = orderType;
        this.lineNumber = lineNumber;
        this.linelessNumber = linelessNumber;
        this.wireRemove = wireRemove;
        this.wirelessRemove = wirelessRemove;
        this.reviseFlag = reviseFlag;
    }

    public AdapterOrderData(String id, String name, String phoneNumber, String phoneName, long time, String address, int orderType
            , int lineNumber, int linelessNumber, int wireRemove, int wirelessRemove, int reviseFlag
            , boolean showNew, boolean showModify, int orderId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.phoneName = phoneName;
        this.time = time;
        this.address = address;
        this.orderType = orderType;
        this.lineNumber = lineNumber;
        this.linelessNumber = linelessNumber;
        this.wireRemove = wireRemove;
        this.wirelessRemove = wirelessRemove;
        this.reviseFlag = reviseFlag;
        this.showNew = showNew;
        this.showModify = showModify;
        this.orderId = orderId;
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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
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

    public int getWireRemove() {
        return wireRemove;
    }

    public void setWireRemove(int wireRemove) {
        this.wireRemove = wireRemove;
    }

    public int getWirelessRemove() {
        return wirelessRemove;
    }

    public void setWirelessRemove(int wirelessRemove) {
        this.wirelessRemove = wirelessRemove;
    }

    public int getReviseFlag() {
        return reviseFlag;
    }

    public void setReviseFlag(int reviseFlag) {
        this.reviseFlag = reviseFlag;
    }

    public boolean isShowNew() {
        return showNew;
    }

    public void setShowNew(boolean showNew) {
        this.showNew = showNew;
    }

    public boolean isShowModify() {
        return showModify;
    }

    public void setShowModify(boolean showModify) {
        this.showModify = showModify;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
