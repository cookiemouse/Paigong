package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterHandledData {
    private String name, time, address, id;
    private int orderType;
    private int online, lineLess;
    private int wireRemove, wirelessRemove;
    private int lastId;

    public AdapterHandledData(String name, String time, String address, String id, int orderType
            , int online, int lineLess, int wireRemove, int wirelessRemove, int lastId) {
        this.name = name;
        this.time = time;
        this.address = address;
        this.id = id;
        this.orderType = orderType;
        this.online = online;
        this.lineLess = lineLess;
        this.wireRemove = wireRemove;
        this.wirelessRemove = wirelessRemove;
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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
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
