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
}
