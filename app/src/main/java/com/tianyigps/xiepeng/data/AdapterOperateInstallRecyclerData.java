package com.tianyigps.xiepeng.data;

import android.net.Uri;

/**
 * Created by cookiemouse on 2017/8/1.
 */

public class AdapterOperateInstallRecyclerData {
    private Uri uri;

    public AdapterOperateInstallRecyclerData() {
    }

    public AdapterOperateInstallRecyclerData(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
