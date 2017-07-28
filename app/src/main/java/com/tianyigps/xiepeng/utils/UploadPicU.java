package com.tianyigps.xiepeng.utils;

import android.support.annotation.Nullable;

import com.tianyigps.xiepeng.interfaces.OnUploadPicListener;
import com.tianyigps.xiepeng.manager.NetworkManager;

/**
 * Created by cookiemouse on 2017/7/28.
 */

public class UploadPicU {
    private static NetworkManager mNetworkManager;

    public UploadPicU() {
        mNetworkManager = new NetworkManager();
    }

    //  上传单张图片
    public void uploadPic(int eid, String token, String orderNo
            , @Nullable int tId, int type, int model
            , @Nullable String imgUrl, String upfile) {
        mNetworkManager.uploadPic(eid, token, orderNo, tId, type, model, imgUrl, upfile);
    }

    //  上传多张图片
    public void uploadPics(int eid, String token, String orderNo
            , @Nullable int carId, @Nullable int tId, int type, int model
            , String... upfiles) {
        mNetworkManager.uploadPic(eid, token, orderNo, carId, tId, type, model, upfiles);
    }


    public void setOnUploadPicListener(OnUploadPicListener listener) {
        mNetworkManager.setOnUploadPicListener(listener);
    }
}
