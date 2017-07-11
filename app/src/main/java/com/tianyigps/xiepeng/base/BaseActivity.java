package com.tianyigps.xiepeng.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tianyigps.xiepeng.R;

public class BaseActivity extends Activity {

    private FrameLayout mFrameLayout;

    private ImageView mImageViewLeft, mImageViewRight;
    private OnTitleRightClickListener mOnTitleRightClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        //透明状态栏
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mFrameLayout = (FrameLayout) findViewById(R.id.fl_activity_base_content);

        mImageViewLeft = findViewById(R.id.iv_activity_base_left);
        mImageViewRight = findViewById(R.id.iv_activity_base_right);

        mImageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnTitleRightClickListener){
                    return;
                }
                mOnTitleRightClickListener.onClick();
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        mFrameLayout.removeAllViews();
        View.inflate(this, layoutResID, mFrameLayout);
        onContentChanged();
    }
    @Override
    public void setContentView(View view) {
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(view, params);
        onContentChanged();
    }

    public interface OnTitleRightClickListener{
        void onClick();
    }

    public void setOnTitleRightClickListener(OnTitleRightClickListener listener){
        this.mOnTitleRightClickListener = listener;
    }
}
