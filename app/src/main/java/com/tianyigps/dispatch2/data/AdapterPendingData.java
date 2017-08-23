package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/11.
 */

public class AdapterPendingData {

    private String order, name, contactName, phoneNumber, time, address;
    int orderType;
    int lineNumber, linelessNumber;
    int wireRemove, wirelessRemove;

    int orderId;

    private int orderStatus;
    private int node;

    public AdapterPendingData(String order, String name, String contactName, String phoneNumber
            , String time, String address, int orderType
            , int lineNumber, int linelessNumber, int orderId, int orderStatus, int node) {
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
        this.orderStatus = orderStatus;
        this.node = node;
    }

    public AdapterPendingData(String order, String name, String contactName, String phoneNumber, String time
            , String address, int orderType, int lineNumber, int linelessNumber, int wireRemove, int wirelessRemove
            , int orderId, int orderStatus, int note) {
        this.order = order;
        this.name = name;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.time = time;
        this.address = address;
        this.orderType = orderType;
        this.lineNumber = lineNumber;
        this.linelessNumber = linelessNumber;
        this.wireRemove = wireRemove;
        this.wirelessRemove = wirelessRemove;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.node = note;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }
}
