package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterHandledData {
    private String name, time, address, id;
    private int online, lineLess;

    public AdapterHandledData(String name, String time, String address, String id, int online, int lineLess) {
        this.name = name;
        this.time = time;
        this.address = address;
        this.id = id;
        this.online = online;
        this.lineLess = lineLess;
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
}
