package com.tianyigps.dispatch2.bean;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class TerminalInfo {
    private int tId;
    private String oldImei;
    private String newImei;
    private int model;
    private int locateType;
    private String newInstallPosition;
    private String repaireRemark;

    public TerminalInfo(int tId, String oldImei, String newImei, int model, int locateType, String newInstallPosition, String repaireRemark) {
        this.tId = tId;
        this.oldImei = oldImei;
        this.newImei = newImei;
        this.model = model;
        this.locateType = locateType;
        this.newInstallPosition = newInstallPosition;
        this.repaireRemark = repaireRemark;
    }

    public TerminalInfo(int tId, String oldImei, String newImei, int model, int locateType, String newInstallPosition) {
        this.tId = tId;
        this.oldImei = oldImei;
        this.newImei = newImei;
        this.model = model;
        this.locateType = locateType;
        this.newInstallPosition = newInstallPosition;
    }
}
