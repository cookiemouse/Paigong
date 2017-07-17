package com.tianyigps.xiepeng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;

public class OrderDetailsActivity extends Activity {

    //Title栏
    private TextView mTextViewTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;

    //  内容
    private TextView mtTextViewOrderName, mTextViewOrderId, mTextViewCallName
            , mTextViewTime, mTextViewAddress, mTextViewRemarks
            , mTextViewInstallTitle, mTextViewInstallContent, mTextViewInfoTitle
            , mTextViewInfoContent;

    private ImageView mImageViewCall;
    private Button mButtonSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_order_details);

        init();

        setEventListener();
    }

    private void init() {
        mTextViewTitle = findViewById(R.id.tv_layout_title_base_middle);
        mImageViewTitleLeft = findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = findViewById(R.id.iv_layout_title_base_right);

        mTextViewTitle.setVisibility(View.GONE);
        mImageViewTitleRight.setVisibility(View.GONE);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_back);

        //  内容
        mtTextViewOrderName = findViewById(R.id.tv_layout_order_details_content_order_title);
        mTextViewOrderId = findViewById(R.id.tv_layout_order_details_content_order_id);
        mTextViewCallName = findViewById(R.id.tv_layout_order_details_content_order_name);
        mTextViewTime = findViewById(R.id.tv_layout_order_details_content_order_time);
        mTextViewAddress = findViewById(R.id.tv_layout_order_details_content_order_address);
        mTextViewRemarks = findViewById(R.id.tv_layout_order_details_content_order_remarks_content);
        mTextViewInstallTitle = findViewById(R.id.tv_layout_order_details_content_order_install_title);
        mTextViewInstallContent = findViewById(R.id.tv_layout_order_details_content_order_install_content);
        mTextViewInfoTitle = findViewById(R.id.tv_layout_order_details_content_order_info_title);
        mTextViewInfoContent = findViewById(R.id.tv_layout_order_details_content_order_info_content);

        mButtonSign = findViewById(R.id.btn_layout_order_details_sign);

        mImageViewCall = findViewById(R.id.iv_layout_order_details_content_call);
    }

    private void setEventListener() {
        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
