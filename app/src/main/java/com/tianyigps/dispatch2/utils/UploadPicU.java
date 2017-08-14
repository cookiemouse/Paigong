package com.tianyigps.dispatch2.utils;

import android.support.annotation.Nullable;

import com.tianyigps.dispatch2.manager.NetworkManager;

/**
 * Created by cookiemouse on 2017/7/28.
 */

public class UploadPicU {
    private NetworkManager mNetworkManager;

    public UploadPicU(NetworkManager networkManager) {
        mNetworkManager = networkManager;
    }

    //  上传单张图片
    public void uploadPic(int eid, String token, String orderNo
            , int carId, @Nullable int tId, int type, int model
            , @Nullable String imgUrl, String upfile, String userName) {
        if (0 == tId || 0 == carId) {
            mNetworkManager.uploadPic(eid, token, orderNo, "" + carId, "", type, model, imgUrl, upfile, userName);
            return;
        }
        mNetworkManager.uploadPic(eid, token, orderNo, "" + carId, "" + tId, type, model, imgUrl, upfile, userName);
    }

    //  上传单张图片
    public void uploadCarPic(int eid, String token, String orderNo
            , @Nullable int carId, int type
            , @Nullable String imgUrl, String upfile, String userName) {
        mNetworkManager.uploadCarPic(eid, token, orderNo, carId, type, imgUrl, upfile, userName);
    }

    //  上传多张图片
//    public void uploadPics(int eid, String token, String orderNo
//            , @Nullable int carId, @Nullable int tId, int type, int model, String userName
//            , String... upfiles) {
//        mNetworkManager.uploadPic(eid, token, orderNo, carId, tId, type, model, userName, upfiles);
//    }
}
