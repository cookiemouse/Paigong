package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class AdapterRepairData {

    private int tId;
    private int type;
    private String id, name, carNo, frameNo;
    //  是否完成，即item背景
    //  complete 0=未check,1=check完成，2=check未完成
    private int complete;
    private int carId;

    public AdapterRepairData(int tId, int type, String id, String name, String carNo, String frameNo, int carId) {
        this.tId = tId;
        this.type = type;
        this.id = id;
        this.name = name;
        this.carNo = carNo;
        this.frameNo = frameNo;
        this.complete = 0;
        this.carId = carId;
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

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
