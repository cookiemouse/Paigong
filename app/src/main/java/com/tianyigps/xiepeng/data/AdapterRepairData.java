package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class AdapterRepairData {
    private String type, id, name, carNo, frameNo;

    public AdapterRepairData(String type, String id, String name, String carNo, String frameNo) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.carNo = carNo;
        this.frameNo = frameNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}
