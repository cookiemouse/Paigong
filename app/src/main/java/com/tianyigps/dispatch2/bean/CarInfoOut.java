package com.tianyigps.dispatch2.bean;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class CarInfoOut {

    private CarInfo carInfo;

    public CarInfoOut(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }
}
