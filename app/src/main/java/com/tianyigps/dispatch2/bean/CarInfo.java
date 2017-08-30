package com.tianyigps.dispatch2.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class CarInfo {
    //    "carId":0,"carNo":"carNo","carVin":"carVin","carBrand":"carBrand"
    private int carId;
    private String carNo;
    private String carVin;
    private String carBrand;
    private List<TerminalInfo> terminalInfo;

    public CarInfo(int carId, String carNo, String carVin, String carBrand, List<TerminalInfo> terminalInfo) {
        this.carId = carId;
        this.carNo = carNo;
        this.carVin = carVin;
        this.carBrand = carBrand;
        this.terminalInfo = terminalInfo;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarVin() {
        return carVin;
    }

    public void setCarVin(String carVin) {
        this.carVin = carVin;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public List<TerminalInfo> getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(List<TerminalInfo> terminalInfo) {
        this.terminalInfo = terminalInfo;
    }
}
