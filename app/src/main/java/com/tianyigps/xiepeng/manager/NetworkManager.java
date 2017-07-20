package com.tianyigps.xiepeng.manager;

import android.util.Log;

import com.tianyigps.xiepeng.data.Urls;
import com.tianyigps.xiepeng.interfaces.OnCheckIMEIListener;
import com.tianyigps.xiepeng.interfaces.OnCheckUserListener;
import com.tianyigps.xiepeng.interfaces.OnContactSiteListener;
import com.tianyigps.xiepeng.interfaces.OnDeletePicListener;
import com.tianyigps.xiepeng.interfaces.OnGetLastInstallerListener;
import com.tianyigps.xiepeng.interfaces.OnGetQualityCountListener;
import com.tianyigps.xiepeng.interfaces.OnGetTerminalInfoListener;
import com.tianyigps.xiepeng.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderHandedListener;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderHandingListener;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderInfoHandingListener;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.xiepeng.interfaces.OnGetWorkerOrderListener;
import com.tianyigps.xiepeng.interfaces.OnInstallBackListener;
import com.tianyigps.xiepeng.interfaces.OnModifyPasswordListener;
import com.tianyigps.xiepeng.interfaces.OnRemoveTerminalInfoListener;
import com.tianyigps.xiepeng.interfaces.OnRemoveTerminalListener;
import com.tianyigps.xiepeng.interfaces.OnSaveOrderInfoListener;
import com.tianyigps.xiepeng.interfaces.OnSignedWorkerListener;
import com.tianyigps.xiepeng.interfaces.OnStartHandingListener;
import com.tianyigps.xiepeng.interfaces.OnUploadPicListener;
import com.tianyigps.xiepeng.utils.MD5U;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by djc on 2017/7/17.
 */

public class NetworkManager {

    private static final String TAG = "NetworkManager";

    private static NetworkManager mNetworkManager;

    private OkHttpClient mOkHttpClient;
    private Request mRequest;

    private OnCheckUserListener mOnCheckUserListener;
    private OnModifyPasswordListener mOnModifyPasswordListener;
    private OnGetWorkerOrderListener mOnGetWorkerOrderListener;
    private OnInstallBackListener mOnInstallBackListener;
    private OnContactSiteListener mOnContactSiteListener;
    private OnSignedWorkerListener mOnSignedWorkerListener;
    private OnGetTerminalInfoListener mOnGetTerminalInfoListener;
    private OnGetWorkerOrderHandingListener mOnGetWorkerOrderHandingListener;
    private OnGetWorkerOrderInfoHandingListener mOnGetWorkerOrderInfoHandingListener;
    private OnStartHandingListener mOnStartHandingListener;
    //  10
    private OnGetWorkerOrderInfoStartListener mOnGetWorkerOrderInfoStartListener;
    private OnRemoveTerminalListener mOnRemoveTerminalListener;
    private OnCheckIMEIListener mOnCheckIMEIListener;
    private OnGetWorkerOrderHandedListener mOnGetWorkerOrderHandedListener;
    private OnGetQualityCountListener mOnGetQualityCountListener;
    private OnGetLastInstallerListener mOnGetLastInstallerListener;
    private OnSaveOrderInfoListener mOnSaveOrderInfoListener;
    private OnRemoveTerminalInfoListener mOnRemoveTerminalInfoListener;
    private OnUploadPicListener mOnUploadPicListener;
    private OnDeletePicListener mOnDeletePicListener;
    //  20
    private OnGetWholeIMEIListener mOnGetWholeIMEIListener;

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

    //  登录  1
    public void checkUser(String userName, String password, String token) {
        Request.Builder builder = new Request.Builder();
        if ("".equals(password)) {
            builder.url(Urls.URL_CHECK_USER + "userName=" + userName + "&token=" + token);
        } else {
            builder.url(Urls.URL_CHECK_USER + "userName=" + userName
                    + "&password=" + MD5U.getMd5(password));
        }
        mRequest = builder.build();
        Log.i(TAG, "checkUser: url-mOnModifyPasswordListener->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnCheckUserListener) {
                    throw new NullPointerException("OnCheckUserListener is null");
                }
                mOnCheckUserListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnCheckUserListener) {
                    throw new NullPointerException("OnCheckUserListener is null");
                }
                mOnCheckUserListener.onSuccess(response.body().string());
            }
        });
    }

    public void setCheckUserListener(OnCheckUserListener listener) {
        this.mOnCheckUserListener = listener;
    }

    //  修改密码    2
    public void modifyPassword(String userName, String token, String oldPwd, String newPwd) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_CHANGE_PSD + "userName=" + userName
                + "&token=" + token
                + "&oldPwd=" + oldPwd
                + "&newPwd=" + newPwd);
        mRequest = builder.build();
        Log.i(TAG, "modifyPassword: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnModifyPasswordListener) {
                    throw new NullPointerException("OnModifyPasswordListener is null");
                }
                mOnModifyPasswordListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnModifyPasswordListener) {
                    throw new NullPointerException("OnModifyPasswordListener is null");
                }
                mOnModifyPasswordListener.onSuccess(response.body().string());
            }
        });
    }

    public void setModifyPasswordListener(OnModifyPasswordListener listener) {
        this.mOnModifyPasswordListener = listener;
    }

    //  获取Worker订单任务    3
    public void getWorkerOrder(int eid, String token, String condition) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER + "eid=" + eid
                + "&token=" + token
                + "condition=" + condition);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrder: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetWorkerOrderListener) {
                    throw new NullPointerException("OnGetWorkerOrderListener is null");
                }
                mOnGetWorkerOrderListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetWorkerOrderListener) {
                    throw new NullPointerException("OnGetWorkerOrderListener is null");
                }
                mOnGetWorkerOrderListener.onSuccess(response.body().string());
            }
        });
    }

    public void setGetWorkerOrderListener(OnGetWorkerOrderListener listener) {
        this.mOnGetWorkerOrderListener = listener;
    }

    //  安装退回    4
    public void installBack(int eid, String token, String orderNo
            , String chooseReason, String filledReason) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_INSTALL_BACK + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&chooseReason=" + chooseReason
                + "filledReason=" + filledReason);
        mRequest = builder.build();
        Log.i(TAG, "installBack: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnInstallBackListener) {
                    throw new NullPointerException("OnInstallBackListener is null");
                }
                mOnInstallBackListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnInstallBackListener) {
                    throw new NullPointerException("OnInstallBackListener is null");
                }
                mOnInstallBackListener.onSuccess(response.body().string());
            }
        });
    }

    public void setInstallBackListener(OnInstallBackListener listener) {
        this.mOnInstallBackListener = listener;
    }

    //  安装工程师联系现场   5
    public void contactSite(int eid, String token, String orderNo, String eName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_CONTACT_SITE + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&eName=" + eName);
        mRequest = builder.build();
        Log.i(TAG, "contactSite: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnContactSiteListener) {
                    throw new NullPointerException("OnContactSiteListener is null");
                }
                mOnContactSiteListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnContactSiteListener) {
                    throw new NullPointerException("OnContactSiteListener is null");
                }
                mOnContactSiteListener.onSuccess(response.body().string());
            }
        });
    }

    public void setContactSiteListener(OnContactSiteListener listener) {
        this.mOnContactSiteListener = listener;
    }

    //  安装工程师签到 6
    public void signedWorker(int eid, String token, String eName
            , String orderNo, double lat, double log
            , String type) {

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
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnSignedWorkerListener) {
                    throw new NullPointerException("OnSignedWorkerListener is null");
                }
                mOnSignedWorkerListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnSignedWorkerListener) {
                    throw new NullPointerException("OnSignedWorkerListener is null");
                }
                mOnSignedWorkerListener.onSuccess(response.body().string());
            }
        });
    }

    public void setSignedWorkerListener(OnSignedWorkerListener listener) {
        this.mOnSignedWorkerListener = listener;
    }

    //  获取设备定位信息    7
    public void getTerminalInfo(int eid, String token, String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_GET_TERMINAL_INFO + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "getTerminalInfo: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetTerminalInfoListener) {
                    throw new NullPointerException("OnGetTerminalInfoListener is null");
                }
                mOnGetTerminalInfoListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetTerminalInfoListener) {
                    throw new NullPointerException("OnGetTerminalInfoListener is null");
                }
                mOnGetTerminalInfoListener.onSuccess(response.body().string());
            }
        });
    }

    public void setGetTerminalInfoListener(OnGetTerminalInfoListener listener) {
        this.mOnGetTerminalInfoListener = listener;
    }

    //  获取安装工程师进行中的订单   8
    public void getWorkerOrderHanding(int eid, String token) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_HANDING + "eid=" + eid
                + "&token=" + token);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderHanding: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetWorkerOrderHandingListener) {
                    throw new NullPointerException("OnGetWorkerOrderHandingListener is null");
                }
                mOnGetWorkerOrderHandingListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetWorkerOrderHandingListener) {
                    throw new NullPointerException("OnGetWorkerOrderHandingListener is null");
                }
                mOnGetWorkerOrderHandingListener.onSuccess(response.body().string());
            }
        });
    }

    public void setGetWorkerOrderHandingListener(OnGetWorkerOrderHandingListener listener) {
        this.mOnGetWorkerOrderHandingListener = listener;
    }

    //  获取安装工程师进行中的订单详情 9
    public void getWorkerOrderInfoHanding(int eid, String token, String orderNo) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_INFO_HANDING + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderInfoHanding: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetWorkerOrderInfoHandingListener) {
                    throw new NullPointerException("OnGetWorkerOrderInfoHandingListener is null");
                }
                mOnGetWorkerOrderInfoHandingListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetWorkerOrderInfoHandingListener) {
                    throw new NullPointerException("OnGetWorkerOrderInfoHandingListener is null");
                }
                mOnGetWorkerOrderInfoHandingListener.onSuccess(response.body().string());
            }
        });
    }

    public void setGetWorkerOrderInfoListener(OnGetWorkerOrderInfoHandingListener listener) {
        this.mOnGetWorkerOrderInfoHandingListener = listener;
    }

    //  安装工程师开始安装   10
    public void startHanding(int eid, String token, String orderNo, String eName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_START_HANDING + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&eName=" + eName);
        mRequest = builder.build();
        Log.i(TAG, "startHanding: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnStartHandingListener) {
                    throw new NullPointerException("OnStartHandingListener is null");
                }
                mOnStartHandingListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnStartHandingListener) {
                    throw new NullPointerException("OnStartHandingListener is null");
                }
                mOnStartHandingListener.onSuccess(response.body().string());
            }
        });
    }

    public void setStartHandingListener(OnStartHandingListener listener) {
        this.mOnStartHandingListener = listener;
    }

    //  获取安装工程师开始安装页面信息 11
    public void getWorkerOrderInfoStart(int eid, String token, String orderNo) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_START_INFO_HANDING + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderInfoStart: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetWorkerOrderInfoStartListener) {
                    throw new NullPointerException("OnGetWorkerOrderInfoStartListener is null");
                }
                mOnGetWorkerOrderInfoStartListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetWorkerOrderInfoStartListener) {
                    throw new NullPointerException("OnGetWorkerOrderInfoStartListener is null");
                }
                mOnGetWorkerOrderInfoStartListener.onSuccess(response.body().string());
            }
        });
    }

    public void setGetWorkerOrderInfoStartListener(OnGetWorkerOrderInfoStartListener listener) {
        this.mOnGetWorkerOrderInfoStartListener = listener;
    }

    //  安装工程师拆除 12
    public void removeTerminal(int eid, String token, String orderNo) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_REMOVE_TERMINAL + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo);
        mRequest = builder.build();
        Log.i(TAG, "removeTerminal: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnRemoveTerminalListener) {
                    throw new NullPointerException("OnRemoveTerminalListener is null");
                }
                mOnRemoveTerminalListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnRemoveTerminalListener) {
                    throw new NullPointerException("OnRemoveTerminalListener is null");
                }
                mOnRemoveTerminalListener.onSuccess(response.body().string());
            }
        });
    }

    public void setRemoveTerminalListener(OnRemoveTerminalListener listener) {
        this.mOnRemoveTerminalListener = listener;
    }

    //  安装工程师设备信息校验 13
    public void checkIMEI(int eid, String token, String imei
            , String model, String orderNo) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_CHECK_IMEI + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei
                + "&model=" + model
                + "&orderNo=" + orderNo);
        mRequest = builder.build();
        Log.i(TAG, "checkIMEI: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnCheckIMEIListener) {
                    throw new NullPointerException("OnCheckIMEIListener is null");
                }
                mOnCheckIMEIListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnCheckIMEIListener) {
                    throw new NullPointerException("OnCheckIMEIListener is null");
                }
                mOnCheckIMEIListener.onSuccess(response.body().string());
            }
        });
    }

    public void setCheckIMEIListener(OnCheckIMEIListener listener) {
        this.mOnCheckIMEIListener = listener;
    }

    //  获取安装工程师已完成订单    14
    public void getWorkerOrderHanded(int eid, String token, String condition
            , String lastOrderId) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_HANDED + "eid=" + eid
                + "&token=" + token
                + "&condition=" + condition
                + "&lastOrderId=" + lastOrderId);
        mRequest = builder.build();
        Log.i(TAG, "getWorkerOrderHanded: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetWorkerOrderHandedListener) {
                    throw new NullPointerException("OnGetWorkerOrderHandedListener is null");
                }
                mOnGetWorkerOrderHandedListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetWorkerOrderHandedListener) {
                    throw new NullPointerException("OnGetWorkerOrderHandedListener is null");
                }
                mOnGetWorkerOrderHandedListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetWorkerOrderHandedListener(OnGetWorkerOrderHandedListener listener) {
        this.mOnGetWorkerOrderHandedListener = listener;
    }

    //  安装工程师质量统计   15
    public void getQualityCount(int eid, String token, String month) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_QUALITY_COUNT + "eid=" + eid
                + "&token=" + token
                + "&month=" + month);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetQualityCountListener) {
                    throw new NullPointerException("OnGetQualityCountListener is null");
                }
                mOnGetQualityCountListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetQualityCountListener) {
                    throw new NullPointerException("OnGetQualityCountListener is null");
                }
                mOnGetQualityCountListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetQualityCountListener(OnGetQualityCountListener listener) {
        this.mOnGetQualityCountListener = listener;
    }

    //  获取历史（最后一个）工程师   16
    public void getLastInstaller(int eid, String token, String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_LAST_INSTALLER + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "getLastInstaller: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetLastInstallerListener) {
                    throw new NullPointerException("OnGetLastInstallerListener is null");
                }
                mOnGetLastInstallerListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetLastInstallerListener) {
                    throw new NullPointerException("OnGetLastInstallerListener is null");
                }
                mOnGetLastInstallerListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetLastInstallerListener(OnGetLastInstallerListener listener) {
        this.mOnGetLastInstallerListener = listener;
    }

    //  提交订单    17
    public void saveOrderInfo(int eid, String token, String orderNo
            , String infoData, String partSubReason, String signature
            , String lat, String log, String type
    ) {

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
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnSaveOrderInfoListener) {
                    throw new NullPointerException("OnSaveOrderInfoListener is null");
                }
                mOnSaveOrderInfoListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnSaveOrderInfoListener) {
                    throw new NullPointerException("OnSaveOrderInfoListener is null");
                }
                mOnSaveOrderInfoListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnSaveOrderInfoListener(OnSaveOrderInfoListener listener) {
        this.mOnSaveOrderInfoListener = listener;
    }

    //  安装工程师删除设备信息 18
    public void removeTerminalInfo(int eid, String token, String orderid
            , String tIds) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_REMOVE_TERMINAL_INFO + "eid=" + eid
                + "&token=" + token
                + "&orderid=" + orderid
                + "&tIds=" + tIds);
        mRequest = builder.build();
        Log.i(TAG, "removeTerminalInfo: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnRemoveTerminalInfoListener) {
                    throw new NullPointerException("OnRemoveTerminalInfoListener is null");
                }
                mOnRemoveTerminalInfoListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnRemoveTerminalInfoListener) {
                    throw new NullPointerException("OnRemoveTerminalInfoListener is null");
                }
                mOnRemoveTerminalInfoListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnRemoveTerminalInfoListener(OnRemoveTerminalInfoListener listener) {
        this.mOnRemoveTerminalInfoListener = listener;
    }

    //  上转图片    19
    public void uploadPic() {
    }

    public void setOnUploadPicListener(OnUploadPicListener listener) {
        this.mOnUploadPicListener = listener;
    }

    //  删除图片    20
    public void deletePic(int eid, String token, String orderNo
            , String carId, String tId, String type
            , String imgUrl) {
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
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnDeletePicListener) {
                    throw new NullPointerException("OnDeletePicListener is null");
                }
                mOnDeletePicListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnDeletePicListener) {
                    throw new NullPointerException("OnDeletePicListener is null");
                }
                mOnDeletePicListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnDeletePicListener(OnDeletePicListener listener) {
        this.mOnDeletePicListener = listener;
    }

    //  获取完整IMEI    21
    public void getWholeImei(int eid, String token, String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_WHOLE_IMEI + "eid=" + eid
                + "&token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "getWholeImei: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetWholeIMEIListener) {
                    throw new NullPointerException("OnGetWholeIMEIListener is null");
                }
                mOnGetWholeIMEIListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetWholeIMEIListener) {
                    throw new NullPointerException("OnGetWholeIMEIListener is null");
                }
                mOnGetWholeIMEIListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetWholeIMEIListener(OnGetWholeIMEIListener listener) {
        this.mOnGetWholeIMEIListener = listener;
    }

    /***************************************华丽的化割线*************************************************/

}
