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

    public AdapterOrderData(String id, String name, String phoneNumber, String phoneName, long time, String address, int orderType, int lineNumber, int linelessNumber, int wireRemove, int wirelessRemove) {
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
}
