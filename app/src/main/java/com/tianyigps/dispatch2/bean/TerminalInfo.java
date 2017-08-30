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

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public String getOldImei() {
        return oldImei;
    }

    public void setOldImei(String oldImei) {
        this.oldImei = oldImei;
    }

    public String getNewImei() {
        return newImei;
    }

    public void setNewImei(String newImei) {
        this.newImei = newImei;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getLocateType() {
        return locateType;
    }

    public void setLocateType(int locateType) {
        this.locateType = locateType;
    }

    public String getNewInstallPosition() {
        return newInstallPosition;
    }

    public void setNewInstallPosition(String newInstallPosition) {
        this.newInstallPosition = newInstallPosition;
    }

    public String getRepaireRemark() {
        return repaireRemark;
    }

    public void setRepaireRemark(String repaireRemark) {
        this.repaireRemark = repaireRemark;
    }
}
