package com.tianyigps.dispatch2.manager;

import android.support.annotation.Nullable;
import android.util.Log;

import com.tianyigps.dispatch2.data.Urls;
import com.tianyigps.dispatch2.interfaces.OnCheckIMEIListener;
import com.tianyigps.dispatch2.interfaces.OnCheckUserListener;
import com.tianyigps.dispatch2.interfaces.OnContactSiteListener;
import com.tianyigps.dispatch2.interfaces.OnDeletePicListener;
import com.tianyigps.dispatch2.interfaces.OnGetLastInstallerListener;
import com.tianyigps.dispatch2.interfaces.OnGetQualityCountListener;
import com.tianyigps.dispatch2.interfaces.OnGetTerminalInfoListener;
import com.tianyigps.dispatch2.interfaces.OnGetWholeIMEIListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderHandedListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderHandingListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoHandingListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderInfoStartListener;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderListener;
import com.tianyigps.dispatch2.interfaces.OnInstallBackListener;
import com.tianyigps.dispatch2.interfaces.OnInstallCountListener;
import com.tianyigps.dispatch2.interfaces.OnModifyDateListener;
import com.tianyigps.dispatch2.interfaces.OnModifyPasswordListener;
import com.tianyigps.dispatch2.interfaces.OnOrderTrackListener;
import com.tianyigps.dispatch2.interfaces.OnPendDetailsListener;
import com.tianyigps.dispatch2.interfaces.OnPendListener;
import com.tianyigps.dispatch2.interfaces.OnPendedListener;
import com.tianyigps.dispatch2.interfaces.OnPendedNumListener;
import com.tianyigps.dispatch2.interfaces.OnPendingNumListener;
import com.tianyigps.dispatch2.interfaces.OnPendingOrderListener;
import com.tianyigps.dispatch2.interfaces.OnRemoveTerminalInfoListener;
import com.tianyigps.dispatch2.interfaces.OnRemoveTerminalListener;
import com.tianyigps.dispatch2.interfaces.OnSaveOrderInfoListener;
import com.tianyigps.dispatch2.interfaces.OnSignedWorkerListener;
import com.tianyigps.dispatch2.interfaces.OnStartHandingListener;
import com.tianyigps.dispatch2.interfaces.OnUploadPicListener;
import com.tianyigps.dispatch2.interfaces.OnWorkersListener;
import com.tianyigps.dispatch2.utils.MD5U;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    //  服务主任
    private OnPendingOrderListener mOnPendingOrderListener;
    private OnWorkersListener mOnWorkersListener;
    private OnInstallCountListener mOnInstallCountListener;
    private OnOrderTrackListener mOnOrderTrackListener;
    private OnPendedListener mOnPendedListener;
    private OnPendListener mOnPendListener;
    private OnPendingNumListener mOnPendingNumListener;
    private OnPendedNumListener mOnPendedNumListener;
    private OnPendDetailsListener mOnPendDetailsListener;
    private OnModifyDateListener mModifyDateListener;

    public NetworkManager() {
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

    @Deprecated
    public void setTimeOut() {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build();
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
        Log.i(TAG, "checkUser: url-OnCheckUserListener->" + mRequest.url());
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
    public void getWorkerOrder(int eid, String token, String condition, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
                + "&condition=" + condition);
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
            , String chooseReason, String filledReason, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_INSTALL_BACK + "eid=" + eid
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&userName=" + userName
                + "&chooseReason=" + chooseReason
                + "&filledReason=" + filledReason);
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
    public void contactSite(int eid, String token, String orderNo, String eName, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_CONTACT_SITE + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
            , String orderNo, double lat, double lng
            , String type, String userName) {

        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_SIGNED + "eid=" + eid
                + "&token=" + token
                + "&eName=" + eName
                + "&userName=" + userName
                + "&orderNo=" + orderNo
                + "&lat=" + lat
                + "&lon=" + lng
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
    public void getTerminalInfo(int eid, String token, String imei, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_GET_TERMINAL_INFO + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
    public void getWorkerOrderHanding(int eid, String token, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_HANDING + "eid=" + eid
                + "&userName=" + userName
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
    public void getWorkerOrderInfoHanding(int eid, String token, String orderNo, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_INFO_HANDING + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
    public void startHanding(int eid, String token, String orderNo, String eName, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_START_HANDING + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
    public void getWorkerOrderInfoStart(int eid, String token, String orderNo, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_START_INFO_HANDING + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
    public void removeTerminal(int eid, String token, String orderNo, String imei, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_REMOVE_TERMINAL + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
                + "&imei=" + imei
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
            , String model, String orderNo, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_CHECK_IMEI + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
            , String lastOrderId, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_ORDER_HANDED + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
    public void getQualityCount(int eid, String token, String month, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_QUALITY_COUNT + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
    public void getLastInstaller(int eid, String token, String imei, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_LAST_INSTALLER + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
            , String lat, String log, String type, String userName) {


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("eid", "" + eid);
        builder.addFormDataPart("token", token);
        builder.addFormDataPart("userName", userName);
        builder.addFormDataPart("orderNo", orderNo);
        builder.addFormDataPart("infoData", ("" + infoData));
        builder.addFormDataPart("partSubReason", ("" + partSubReason));
        builder.addFormDataPart("signature", signature);
        builder.addFormDataPart("lat", lat);
        builder.addFormDataPart("lon", log);
        builder.addFormDataPart("type", type);

        Log.i(TAG, "uploadPic: eid-->" + eid);
        Log.i(TAG, "uploadPic: token-->" + token);
        Log.i(TAG, "uploadPic: orderNo-->" + orderNo);
        Log.i(TAG, "uploadPic: infoData-->" + infoData);
        Log.i(TAG, "uploadPic: partSubReason-->" + partSubReason);
        Log.i(TAG, "uploadPic: signature-->" + signature);
        Log.i(TAG, "uploadPic: lat-->" + lat);
        Log.i(TAG, "uploadPic: lon-->" + log);
        Log.i(TAG, "uploadPic: type-->" + type);

        RequestBody requestBody = builder.build();

        mRequest = new Request.Builder()
                .url(Urls.URL_WORKER_SAVE_ORDER_INFO)
                .post(requestBody)
                .build();

        Log.i(TAG, "uploadPic: url-->" + mRequest.url());

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
            , String tIds, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_REMOVE_TERMINAL_INFO + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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
    public void uploadPic(int eid, String token, String orderNo
            , String carId, String tId, int type, int model
            , @Nullable String imgUrl, String upfile, String userName) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        File file = new File(upfile);
        if (file.exists()) {
            builder.addFormDataPart("upfile"
                    , file.getName()
                    , RequestBody.create(MediaType.parse("image/png"), file));
        }

        builder.addFormDataPart("eid", "" + eid);
        builder.addFormDataPart("token", token);
        builder.addFormDataPart("userName", userName);
        builder.addFormDataPart("orderNo", orderNo);
        builder.addFormDataPart("carId", "" + carId);
        builder.addFormDataPart("tId", "" + tId);
        builder.addFormDataPart("type", ("" + type));
        builder.addFormDataPart("model", ("" + model));
        if (null != imgUrl) {
            builder.addFormDataPart("imgUrl", ("" + imgUrl));
        }

        Log.i(TAG, "uploadPic: eid-->" + eid);
        Log.i(TAG, "uploadPic: token-->" + token);
        Log.i(TAG, "uploadPic: orderNo-->" + orderNo);
        Log.i(TAG, "uploadPic: carId-->" + carId);
        Log.i(TAG, "uploadPic: tId-->" + tId);
        Log.i(TAG, "uploadPic: type-->" + type);
        Log.i(TAG, "uploadPic: model-->" + model);
        Log.i(TAG, "uploadPic: imgUrl-->" + imgUrl);

        RequestBody requestBody = builder.build();

        mRequest = new Request.Builder()
                .url(Urls.URL_WORKER_UPLOAD_PIC)
                .post(requestBody)
                .build();

        Log.i(TAG, "uploadPic: url-->" + mRequest.url());

        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnUploadPicListener) {
                    throw new NullPointerException("OnUploadPicListener is null");
                }
                mOnUploadPicListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnUploadPicListener) {
                    throw new NullPointerException("OnUploadPicListener is null");
                }
                mOnUploadPicListener.onSuccess(response.body().string());
            }
        });
    }

    //  上转图片    19-1
    public void uploadCarPic(int eid, String token, String orderNo
            , @Nullable int carId, int type
            , @Nullable String imgUrl, String upfile, String userName) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        File file = new File(upfile);
        if (file.exists()) {
            builder.addFormDataPart("upfile"
                    , file.getName()
                    , RequestBody.create(MediaType.parse("image/png"), file));
        }

        builder.addFormDataPart("eid", "" + eid);
        builder.addFormDataPart("token", token);
        builder.addFormDataPart("userName", userName);
        builder.addFormDataPart("orderNo", orderNo);
        builder.addFormDataPart("carId", ("" + carId));
        builder.addFormDataPart("type", ("" + type));
        if (null != imgUrl) {
            builder.addFormDataPart("imgUrl", ("" + imgUrl));
        }

        RequestBody requestBody = builder.build();

        mRequest = new Request.Builder()
                .url(Urls.URL_WORKER_UPLOAD_PIC)
                .post(requestBody)
                .build();

        Log.i(TAG, "uploadPic: url-->" + mRequest.url());

        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnUploadPicListener) {
                    throw new NullPointerException("OnUploadPicListener is null");
                }
                mOnUploadPicListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnUploadPicListener) {
                    throw new NullPointerException("OnUploadPicListener is null");
                }
                mOnUploadPicListener.onSuccess(response.body().string());
            }
        });
    }

    //  上转图片    19-2
    /*
    public void uploadPic(int eid, String token, String orderNo
            , int carId, int tId, int type, int model, String userName
            , String... upfiles) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("eid", "" + eid);
        builder.addFormDataPart("token", token);
        builder.addFormDataPart("userName", "" + userName);
        builder.addFormDataPart("orderNo", orderNo);
        builder.addFormDataPart("carId", "" + carId);
        builder.addFormDataPart("tId", "" + tId);
        builder.addFormDataPart("type", "" + type);
        builder.addFormDataPart("model", "" + model);
        for (String path : upfiles) {
            File f = new File(path);
            if (f.exists()) {
                builder.addFormDataPart("upfile"
                        , f.getName()
                        , RequestBody.create(MediaType.parse("image/png"), f));
            }
        }

        MultipartBody multipartBody = builder.build();
        mRequest = new Request.Builder()
                .url(Urls.URL_WORKER_UPLOAD_PIC)
                .post(multipartBody)
                .build();
        Log.i(TAG, "uploadPic: url-->" + mRequest.url());

        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            if (null == mOnUploadPicListener) {
                throw new NullPointerException("OnUploadPicListener is null");
            }
            mOnUploadPicListener.onFailure();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (null == mOnUploadPicListener) {
                throw new NullPointerException("OnUploadPicListener is null");
            }
            mOnUploadPicListener.onSuccess(response.body().string());
        }
    });
}
    */

    public void setOnUploadPicListener(OnUploadPicListener listener) {
        this.mOnUploadPicListener = listener;
    }

    //  删除图片    20
    public void deletePic(int eid, String token, String orderNo
            , int carId, int type
            , String imgUrl, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_DELETE_PIC + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
                + "&orderNo=" + orderNo
                + "&carId=" + carId
                + "&type=" + type
                + "&imgUrl=" + imgUrl);
        mRequest = builder.build();
        Log.i(TAG, "uploadPic: url-->" + mRequest.url());
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
    public void getWholeImei(int eid, String token, String imei, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_WORKER_WHOLE_IMEI + "eid=" + eid
                + "&token=" + token
                + "&userName=" + userName
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

    //  查询待处理订单 22
    public void getPendingOrder(String jobNo, String token, String status, String condition, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_PENDING_ORDER + "jobNo=" + jobNo
                + "&token=" + token
                + "&userName=" + userName
                + "&status=" + status
                + "&condition=" + condition);
        mRequest = builder.build();
        Log.i(TAG, "getPendingOrder: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnPendingOrderListener) {
                    throw new NullPointerException("OnPendingOrderListener is null");
                }
                mOnPendingOrderListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnPendingOrderListener) {
                    throw new NullPointerException("OnPendingOrderListener is null");
                }
                mOnPendingOrderListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnPendingOrderListener(OnPendingOrderListener listener) {
        this.mOnPendingOrderListener = listener;
    }

    /**
     * 获取所有的工程师 22
     *
     * @param token 令牌
     */
    public void getWorkers(String jobNo, String token, String condition, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_WORKER + "jobNo=" + jobNo
                + "&token=" + token
                + "&userName=" + userName
                + "&condition=" + condition);
        mRequest = builder.build();
        Log.i(TAG, "getPendingOrder: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnWorkersListener) {
                    throw new NullPointerException("OnWorkersListener is null");
                }
                mOnWorkersListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnWorkersListener) {
                    throw new NullPointerException("OnWorkersListener is null");
                }
                mOnWorkersListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnWorkersListener(OnWorkersListener listener) {
        this.mOnWorkersListener = listener;
    }

    //  获取服务主任安装统计  23
    public void getInstallCount(String jobNo, String token, String month, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_INSTALL_COUNT + "jobNo=" + jobNo
                + "&token=" + token
                + "&userName=" + userName
                + "&month=" + month);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnInstallCountListener) {
                    throw new NullPointerException("OnInstallCountListener is null");
                }
                mOnInstallCountListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnInstallCountListener) {
                    throw new NullPointerException("OnInstallCountListener is null");
                }
                mOnInstallCountListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnInstallCountListener(OnInstallCountListener listener) {
        this.mOnInstallCountListener = listener;
    }

    //  获取订单跟踪信息    24
    public void getOrderTrack(String jobNo, String token, String userName, int orderId) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_ORDER_TRACK + "jobNo=" + jobNo
                + "&token=" + token
                + "&userName=" + userName
                + "&orderId=" + orderId);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnOrderTrackListener) {
                    throw new NullPointerException("OnOrderTrackListener is null");
                }
                mOnOrderTrackListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnOrderTrackListener) {
                    throw new NullPointerException("OnOrderTrackListener is null");
                }
                mOnOrderTrackListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnOrderTrackListener(OnOrderTrackListener listener) {
        this.mOnOrderTrackListener = listener;
    }

    //  获取历史订单列表    25
    public void getPended(String jobNo, String token, String status, String condition, String lastOrderId, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_ORDER_PENDED + "jobNo=" + jobNo
                + "&token=" + token
                + "&status=" + status
                + "&condition=" + condition
                + "&lastOrderId=" + lastOrderId
                + "&userName=" + userName);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnPendedListener) {
                    throw new NullPointerException("OnPendedListener is null");
                }
                mOnPendedListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnPendedListener) {
                    throw new NullPointerException("OnPendedListener is null");
                }
                mOnPendedListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnPendedListener(OnPendedListener listener) {
        this.mOnPendedListener = listener;
    }

    //  派单      26
    public void pendOrder(String jobNo, String userName, String token, String orderNo, int orderStatus, int eid
            , int isPay) {

        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_PEND + "jobNo=" + jobNo
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&orderStatus=" + orderStatus
                + "&eid=" + eid
                + "&isPay=" + isPay
                + "&userName=" + userName);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnPendListener) {
                    throw new NullPointerException("OnPendListener is null");
                }
                mOnPendListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnPendListener) {
                    throw new NullPointerException("OnPendListener is null");
                }
                mOnPendListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnPendListener(OnPendListener listener) {
        this.mOnPendListener = listener;
    }

    //  获取待处理数量     27
    public void getPendingNum(String jobNo, String token, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_PENDING_NUM + "jobNo=" + jobNo
                + "&token=" + token
                + "&userName=" + userName);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnPendingNumListener) {
                    throw new NullPointerException("OnPendingNumListener is null");
                }
                mOnPendingNumListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnPendingNumListener) {
                    throw new NullPointerException("OnPendingNumListener is null");
                }
                mOnPendingNumListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnPendingNumListener(OnPendingNumListener listener) {
        this.mOnPendingNumListener = listener;
    }

    //  获取已处理数量     28
    public void getPendedNum(String jobNo, String token, String userName) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_PENDED_NUM + "jobNo=" + jobNo
                + "&token=" + token
                + "&userName=" + userName);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnPendedNumListener) {
                    throw new NullPointerException("OnPendedNumListener is null");
                }
                mOnPendedNumListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnPendedNumListener) {
                    throw new NullPointerException("OnPendedNumListener is null");
                }
                mOnPendedNumListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnPendedNumListener(OnPendedNumListener listener) {
        this.mOnPendedNumListener = listener;
    }

    //  获取派单详情  29
    public void getPendDetails(String jobNo, String token, String orderNo, String userName, int orderStatus) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_PENDED_DETAILS + "jobNo=" + jobNo
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&orderStatus=" + orderStatus
                + "&userName=" + userName);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnPendDetailsListener) {
                    throw new NullPointerException("OnPendDetailsListener is null");
                }
                mOnPendDetailsListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnPendDetailsListener) {
                    throw new NullPointerException("OnPendDetailsListener is null");
                }
                mOnPendDetailsListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnPendDetailsListener(OnPendDetailsListener listener) {
        this.mOnPendDetailsListener = listener;
    }

    //  改约  30
    public void modifyDate(String jobNo, String userName, String token, String orderNo, int orderStatus, float newDoorDate, String reason) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.URL_MANAGER_MODIFY_DATE + "jobNo=" + jobNo
                + "&token=" + token
                + "&orderNo=" + orderNo
                + "&orderStatus=" + orderStatus
                + "&newDoorDate=" + newDoorDate
                + "&reason=" + reason
                + "&userName=" + userName);
        mRequest = builder.build();
        Log.i(TAG, "getQualityCount: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mModifyDateListener) {
                    throw new NullPointerException("ModifyDateListener is null");
                }
                mModifyDateListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mModifyDateListener) {
                    throw new NullPointerException("ModifyDateListener is null");
                }
                mModifyDateListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnModifyDateListener(OnModifyDateListener listener) {
        this.mModifyDateListener = listener;
    }

}
