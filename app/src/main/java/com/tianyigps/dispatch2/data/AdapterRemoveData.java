package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class AdapterRemoveData {
    private int carId;
    //  type表示拆除和安装，0 = title, 1 = 拆除，2 = 安装
    private int type;
    private String typeName;

    private String frameNo;
    private int online, offline;
    private int onlineComplete, offlineComplete;

    public AdapterRemoveData(String typeName) {
        this.type = 0;
        this.typeName = typeName;
    }

    public AdapterRemoveData(int online, int offline) {
        this.type = 1;
        this.online = online;
        this.offline = offline;
    }

    public AdapterRemoveData(int carId, int online, int offline, int onlineComplete, int offlineComplete) {
        this.type = 1;
        this.carId = carId;
        this.online = online;
        this.offline = offline;
        this.onlineComplete = onlineComplete;
        this.offlineComplete = offlineComplete;
    }

    public AdapterRemoveData(int carId, String frameNo, int online, int offline) {
        this.type = 2;
        this.carId = carId;
        this.typeName = "";
        this.frameNo = frameNo;
        this.online = online;
        this.offline = offline;
    }

    public AdapterRemoveData(int carId, String frameNo, int online, int offline, int onlineComplete, int
            offlineComplete) {
        this.type = 2;
        this.carId = carId;
        this.typeName = "";
        this.frameNo = frameNo;
        this.online = online;
        this.offline = offline;
        this.onlineComplete = onlineComplete;
        this.offlineComplete = offlineComplete;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getFrameNo() {
        return frameNo;
    }

    public void setFrameNo(String frameNo) {
        this.frameNo = frameNo;
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

    public int getOnlineComplete() {
        return onlineComplete;
    }

    public void setOnlineComplete(int onlineComplete) {
        this.onlineComplete = onlineComplete;
    }

    public int getOfflineComplete() {
        return offlineComplete;
    }

    public void setOfflineComplete(int offlineComplete) {
        this.offlineComplete = offlineComplete;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
