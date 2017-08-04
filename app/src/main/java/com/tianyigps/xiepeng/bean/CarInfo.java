package com.tianyigps.xiepeng.bean;

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

    public static class TerminalInfo {
        //        {"tid":0,"oldImei":"imeiOld","newImei":"imeiNew","modle":1,"locateType":1,"newInstallPosition":""}
        private int tid;
        private String oldImei;
        private String newImei;
        private int modle;
        private int locateType;
        private String newInstallPosition;

        public TerminalInfo(int tid, String oldImei, String newImei, int modle, int locateType, String newInstallPosition) {
            this.tid = tid;
            this.oldImei = oldImei;
            this.newImei = newImei;
            this.modle = modle;
            this.locateType = locateType;
            this.newInstallPosition = newInstallPosition;
        }
    }
}
