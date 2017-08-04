package com.tianyigps.xiepeng.bean;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class TerminalInfo {
    private int tid;
    private String oldImei;
    private String newImei;
    private int modle;
    private int locateType;
    private String newInstallPosition;
    private String repaireRemark;

    public TerminalInfo(int tid, String oldImei, String newImei, int modle, int locateType, String newInstallPosition, String repaireRemark) {
        this.tid = tid;
        this.oldImei = oldImei;
        this.newImei = newImei;
        this.modle = modle;
        this.locateType = locateType;
        this.newInstallPosition = newInstallPosition;
        this.repaireRemark = repaireRemark;
    }

    public TerminalInfo(int tid, String oldImei, String newImei, int modle, int locateType, String newInstallPosition) {
        this.tid = tid;
        this.oldImei = oldImei;
        this.newImei = newImei;
        this.modle = modle;
        this.locateType = locateType;
        this.newInstallPosition = newInstallPosition;
    }
}
