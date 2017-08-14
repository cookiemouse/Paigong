package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2017/8/1.
 */

public class AdapterOperateInstallRecyclerData {
    private String path;
    private String imgUrl;

    public AdapterOperateInstallRecyclerData() {
    }

    public AdapterOperateInstallRecyclerData(String path, String imgUrl) {
        this.path = path;
        this.imgUrl = imgUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
