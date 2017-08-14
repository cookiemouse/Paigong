package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterHandledData {
    private String name, time, address, id, orderType;
    private int online, lineLess;

    private int lastId;

    public AdapterHandledData(String name, String time, String address, String id, String orderType
            , int online, int lineLess, int lastId) {
        this.name = name;
        this.time = time;
        this.address = address;
        this.id = id;
        this.orderType = orderType;
        this.online = online;
        this.lineLess = lineLess;
        this.lastId = lastId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getLineLess() {
        return lineLess;
    }

    public void setLineLess(int lineLess) {
        this.lineLess = lineLess;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }
}
