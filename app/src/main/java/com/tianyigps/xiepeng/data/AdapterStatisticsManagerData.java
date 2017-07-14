package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/14.
 */

public class AdapterStatisticsManagerData {

    private String name;
    private int door, carNumber, online, offline;

    public AdapterStatisticsManagerData(String name, int door, int carNumber, int online, int offline) {
        this.name = name;
        this.door = door;
        this.carNumber = carNumber;
        this.online = online;
        this.offline = offline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public int getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOffline() {
        return offline;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }
}
