package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class AdapterRepairData {
    private int type;
    private String id, name, carNo, frameNo;
    //  是否完成，即item背景
    private boolean complete;

    public AdapterRepairData(int type, String id, String name, String carNo, String frameNo) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.carNo = carNo;
        this.frameNo = frameNo;
        this.complete = true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getFrameNo() {
        return frameNo;
    }

    public void setFrameNo(String frameNo) {
        this.frameNo = frameNo;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
