package com.tianyigps.xiepeng.manager;

import android.util.Log;

import com.tianyigps.xiepeng.data.Urls;
import com.tianyigps.xiepeng.utils.MD5U;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by djc on 2017/7/17.
 */

public class NetworkManager {

    private static final String TAG = "NetworkManager";

    private static NetworkManager mNetworkManager;

    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private Callback mCallback;

    private NetworkManager() {
        mOkHttpClient = new OkHttpClient();
    }

    public static NetworkManager getInstance() {
        if (null == mNetworkManager) {
            synchronized (NetworkManager.class) {
                if (null == mNetworkManager) {
                    mNetworkManager = new NetworkManager();
                }
            }
        }

        return mNetworkManager;
    }

    private void start() {
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(mCallback);
    }

    public void reStart(){
        start();
    }

    //  登录
    public void checkUser(String userName, String password, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_CHECK_USER + "userName=" + userName + "&password=" + MD5U.getMd5(password));
        mRequest = builder.build();
        Log.i(TAG, "checkUser: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  修改密码
    public void modifyPassword(String userName, String token, String oldPwd, String newPwd, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_CHANGE_PSD + "userName=" + userName
                + "&token=" + token
                + "oldPwd=" + oldPwd
                + "newPwd=" + newPwd);
        mRequest = builder.build();
        Log.i(TAG, "modifyPassword: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取Worker订单任务
    public void getWorkerOrder(String eid, String token, String condition, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER + "eid=" + eid
                + "&token=" + token
                + "condition=" + condition);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrder: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装退回
    public void installBack(String eid, String token, String orderNo
            , String chooseReason, String filledReason, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_INSTALL_BACK + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&chooseReason=" + chooseReason
                + "filledReason=" + filledReason);
        mRequest = builder.build();
        Log.i(TAG, "installBack: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装工程师联系现场
    public void contactSite(String eid, String token, String orderNo, String eName, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_CONTACT_SITE + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&eName=" + eName);
        mRequest = builder.build();
        Log.i(TAG, "contactSite: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装工程师签到
    public void signedWorker(String eid, String token, String eName
            , String orderNo, String lat, String log
            , String type, Callback callback) {

        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_SIGNED + "eid=" + eid
                + "&token=" + token
                + "&eName=" + eName
                + "&orderNo=" + orderNo
                + "&lat=" + lat
                + "&log=" + log
                + "&type=" + type);
        mRequest = builder.build();
        Log.i(TAG, "signedWorker: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取设备定位信息
    public void getTerminalInfo(String eid, String token, String imei, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_GET_TERMINAL_INFO + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "getTerminalInfo: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取安装工程师进行中的订单
    public void getWorkerOrderHanding(String eid, String token, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_HANDING + "eid=" + eid
                + "&token=" + token);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderHanding: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取安装工程师进行中的订单详情
    public void getWorkerOrderInfoHanding(String eid, String token, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_INFO_HANDING + "eid=" + eid
                + "&token=" + token);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderInfoHanding: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装工程师开始安装
    public void startHanding(String eid, String token, String orderNo, String eName, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_START_HANDING + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&eName=" + eName);
        mRequest = builder.build();
        Log.i(TAG, "startHanding: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取安装工程师开始安装页面信息
    public void getWorkerOrderInfoStart(String eid, String token, String orderNo, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_START_INFO_HANDING + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderInfoStart: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装工程师拆除
    public void removeTerminal(String eid, String token, String orderNo, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_REMOVE_TERMINAL + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo);
        mRequest = builder.build();
        Log.i(TAG, "removeTerminal: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装工程师设备信息校验
    public void checkIMEI(String eid, String token, String imei
            , String model, String orderNo, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_CHECK_IMEI + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei
                + "&model=" + model
                + "&orderNo=" + orderNo);
        mRequest = builder.build();
        Log.i(TAG, "checkIMEI: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取安装工程师已完成订单
    public void getWorkerOrderHanded(String eid, String token, String condition
            , String lastOrderId, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_HANDED + "eid=" + eid
                + "&token=" + token
                + "&condition=" + condition
                + "&lastOrderId=" + lastOrderId);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderHanded: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装工程师质量统计
    public void getQualityCount(String eid, String token, String month, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_QUALITY_COUNT + "eid=" + eid
                + "&token=" + token
                + "&month=" + month);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取历史（最后一个）工程师
    public void getLastInstaller(String eid, String token, String imei, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_LAST_INSTALLER + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "getLastInstaller: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  提交订单
    public void saveOrderInfo(String eid, String token, String orderNo
            , String infoData, String partSubReason, String signature
            , String lat, String log, String type
            , Callback callback) {

        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_SAVE_ORDER_INFO + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&infoData=" + infoData
                + "&partSubReason=" + partSubReason
                + "&signature=" + signature
                + "&lat=" + lat
                + "&log=" + log
                + "&type=" + type);
        mRequest = builder.build();
        Log.i(TAG, "saveOrderInfo: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  安装工程师删除设备信息
    public void removeTerminalInfo(String eid, String token, String orderid
            , String tIds, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_REMOVE_TERMINAL_INFO + "eid=" + eid
                + "&token=" + token
                + "&orderid=" + orderid
                + "&tIds=" + tIds);
        mRequest = builder.build();
        Log.i(TAG, "removeTerminalInfo: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  上转图片
    public void uploadPic() {
    }

    //  删除图片
    public void deletePic(String eid, String token, String orderNo
            , String carId, String tId, String type
            , String imgUrl, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_DELETE_PIC + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&carId=" + carId
                + "&tId=" + tId
                + "&type=" + type
                + "&imgUrl=" + imgUrl);
        mRequest = builder.build();
        Log.i(TAG, "deletePic: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    //  获取完速IMEI
    public void getWholeImei(String eid, String token, String imei, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_WHOLE_IMEI + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "getWholeImei: url-->" + mRequest.url());
        this.mCallback = callback;
        start();
    }

    /***************************************华丽的化割线*************************************************/
}
