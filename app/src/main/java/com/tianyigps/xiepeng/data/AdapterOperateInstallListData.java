package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class AdapterOperateInstallListData {
    private String tNoNew, position;
    private String positionPic, installPic;
    private String positionPicUrl, installPicUrl;

    private String tNoOld;

    public AdapterOperateInstallListData(String tNoNew, String position, String positionPic, String installPic, String positionPicUrl, String installPicUrl) {
        this.tNoNew = tNoNew;
        this.position = position;
        this.positionPic = positionPic;
        this.installPic = installPic;
        this.positionPicUrl = positionPicUrl;
        this.installPicUrl = installPicUrl;
    }

    @Deprecated
    public AdapterOperateInstallListData(String tNoNew, String position, String positionPic, String installPic, String
            positionPicUrl, String installPicUrl, String tNoOld) {
        this.tNoNew = tNoNew;
        this.position = position;
        this.positionPic = positionPic;
        this.installPic = installPic;
        this.positionPicUrl = positionPicUrl;
        this.installPicUrl = installPicUrl;
        this.tNoOld = tNoOld;
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
}
