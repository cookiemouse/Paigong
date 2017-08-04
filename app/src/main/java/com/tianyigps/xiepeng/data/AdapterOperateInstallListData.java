package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class AdapterOperateInstallListData {
    private boolean wire;   //  true = 有线，false = 无线
    private String tNoNew, position;
    private String positionPic, installPic;
    private String positionPicUrl, installPicUrl;

    private String tNoOld;

    private boolean complete = true;

    public AdapterOperateInstallListData(boolean wire) {
        this.wire = wire;
    }

    public AdapterOperateInstallListData(boolean wire, String tNoNew, String position, String positionPic, String installPic,
                                         String positionPicUrl, String installPicUrl) {
        this.wire = wire;
        this.tNoNew = tNoNew;
        this.position = position;
        this.positionPic = positionPic;
        this.installPic = installPic;
        this.positionPicUrl = positionPicUrl;
        this.installPicUrl = installPicUrl;
    }

    @Deprecated
    public AdapterOperateInstallListData(boolean wire, String tNoNew, String position, String positionPic, String installPic
            , String positionPicUrl, String installPicUrl, String tNoOld) {
        this.wire = wire;
        this.tNoNew = tNoNew;
        this.position = position;
        this.positionPic = positionPic;
        this.installPic = installPic;
        this.positionPicUrl = positionPicUrl;
        this.installPicUrl = installPicUrl;
        this.tNoOld = tNoOld;
    }

    public boolean isWire() {
        return wire;
    }

    public void setWire(boolean wire) {
        this.wire = wire;
    }

    public String gettNoNew() {
        return tNoNew;
    }

    public void settNoNew(String tNoNew) {
        this.tNoNew = tNoNew;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionPic() {
        return positionPic;
    }

    public void setPositionPic(String positionPic) {
        this.positionPic = positionPic;
    }

    public String getInstallPic() {
        return installPic;
    }

    public void setInstallPic(String installPic) {
        this.installPic = installPic;
    }

    public String getPositionPicUrl() {
        return positionPicUrl;
    }

    public void setPositionPicUrl(String positionPicUrl) {
        this.positionPicUrl = positionPicUrl;
    }

    public String getInstallPicUrl() {
        return installPicUrl;
    }

    public void setInstallPicUrl(String installPicUrl) {
        this.installPicUrl = installPicUrl;
    }

    public String gettNoOld() {
        return tNoOld;
    }

    public void settNoOld(String tNoOld) {
        this.tNoOld = tNoOld;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
