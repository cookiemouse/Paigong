package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/7/25.
 */

public class AdapterOperateRemoveData {
    private String carNo, frameNo, terminalType, terminalName, tNo, installPosition;
    private String picPosition, picInstall;
    private String date;
    private String installName, installPhone;

    public AdapterOperateRemoveData(String carNo, String frameNo, String terminalType, String terminalName, String tNo, String installPosition, String picPosition, String picInstall, String date, String installName, String installPhone) {
        this.carNo = carNo;
        this.frameNo = frameNo;
        this.terminalType = terminalType;
        this.terminalName = terminalName;
        this.tNo = tNo;
        this.installPosition = installPosition;
        this.picPosition = picPosition;
        this.picInstall = picInstall;
        this.date = date;
        this.installName = installName;
        this.installPhone = installPhone;
    }

    public String gettNo() {
        return tNo;
    }

    public void settNo(String tNo) {
        this.tNo = tNo;
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

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getInstallPosition() {
        return installPosition;
    }

    public void setInstallPosition(String installPosition) {
        this.installPosition = installPosition;
    }

    public String getPicPosition() {
        return picPosition;
    }

    public void setPicPosition(String picPosition) {
        this.picPosition = picPosition;
    }

    public String getPicInstall() {
        return picInstall;
    }

    public void setPicInstall(String picInstall) {
        this.picInstall = picInstall;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInstallName() {
        return installName;
    }

    public void setInstallName(String installName) {
        this.installName = installName;
    }

    public String getInstallPhone() {
        return installPhone;
    }

    public void setInstallPhone(String installPhone) {
        this.installPhone = installPhone;
    }
}
