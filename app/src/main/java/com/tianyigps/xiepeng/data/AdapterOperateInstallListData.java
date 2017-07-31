package com.tianyigps.xiepeng.data;

import android.net.Uri;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class AdapterOperateInstallListData {
    private String tNoNew, position;
    private Uri positionPic, installPic;

    private String tNoOld;

    public AdapterOperateInstallListData(String tNoNew, String position, Uri positionPic, Uri installPic) {
        this.tNoNew = tNoNew;
        this.position = position;
        this.positionPic = positionPic;
        this.installPic = installPic;
    }

    @Deprecated
    public AdapterOperateInstallListData(String tNoNew, String position, Uri positionPic, Uri installPic, String tNoOld) {
        this.tNoNew = tNoNew;
        this.position = position;
        this.positionPic = positionPic;
        this.installPic = installPic;
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

    public Uri getPositionPic() {
        return positionPic;
    }

    public void setPositionPic(Uri positionPic) {
        this.positionPic = positionPic;
    }

    public Uri getInstallPic() {
        return installPic;
    }

    public void setInstallPic(Uri installPic) {
        this.installPic = installPic;
    }

    public String gettNoOld() {
        return tNoOld;
    }

    public void settNoOld(String tNoOld) {
        this.tNoOld = tNoOld;
    }
}
