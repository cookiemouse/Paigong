package com.tianyigps.xiepeng.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tianyigps.signviewlibrary.SignView;
import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.base.BaseActivity;
import com.tianyigps.xiepeng.bean.UploadInstallBean;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.manager.FileManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;
import com.tianyigps.xiepeng.utils.Base64U;

import java.util.ArrayList;
import java.util.List;

public class CustomSignActivity extends BaseActivity {

    private static final String TAG = "CustomSignActivity";

    private SignView mSignView;
    private LinearLayout mLinearLayoutClear;
    private Button mButtonSubmit;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int eid;
    private String token;
    private String orderNo;
    private String partReason;
    private String signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sign);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("客户签字");

        mSignView = findViewById(R.id.sv_activity_custom_sign);
        mLinearLayoutClear = findViewById(R.id.ll_activity_custom_sign);
        mButtonSubmit = findViewById(R.id.btn_activity_custom_sign_submit);
    }

    private void setEventListener() {
        mLinearLayoutClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignView.clearPath();
            }
        });

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/7/20 提交

                final Bitmap bitmap = mSignView.getPic();

                String base64 = Base64U.encode(bitmap);

                base64 = Data.DATA_PIC_SIGN_HEAD + base64;

                Log.i(TAG, "onClick: base64-->" + base64);


                //  json
                Gson gson = new Gson();
                List<UploadInstallBean> uploadInstallBeanList = new ArrayList<>();

                UploadInstallBean uploadInstallBean = new UploadInstallBean();
                UploadInstallBean.CarInfoBean carInfoBean = new UploadInstallBean.CarInfoBean();
                UploadInstallBean.CarInfoBean.TerminalInfoBean terminalInfoBean =
                        new UploadInstallBean.CarInfoBean.TerminalInfoBean();

                carInfoBean.setCarId(123);
                carInfoBean.setCarNo("777888999");
                carInfoBean.setCarBrand("BMW");
                carInfoBean.setCarVin("9879865466432");
//                carInfoBean.setTerminalInfo();
                terminalInfoBean.setLocateType(1);

                uploadInstallBean.setCarInfo(carInfoBean);

                uploadInstallBeanList.add(uploadInstallBean);

                String json = gson.toJson(uploadInstallBeanList);
                Log.i(TAG, "onClick: json-->" + json);

                //保存图片，可以不要
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileManager f = new FileManager(Data.DATA_PIC_SIGN);
                        f.saveBitmapPng(bitmap);
                    }
                });
                thread.start();
            }
        });
    }
}
