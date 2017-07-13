package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterHandingData {
    private String name, time, address, id, callName, callNumber;
    private int online, lineless;

    public AdapterHandingData(String name, String time, String address, String id, String callName, String callNumber, int online, int lineless) {
        this.name = name;
        this.time = time;
        this.address = address;
        this.id = id;
        this.callName = callName;
        this.callNumber = callNumber;
        this.online = online;
        this.lineless = lineless;
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
}
