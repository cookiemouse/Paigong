package com.tianyigps.xiepeng.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;

public class BaseActivity extends Activity {

    private FrameLayout mFrameLayout;

    private TextView mTextViewTitle;
    private ImageView mImageViewLeft, mImageViewRight;
    private LinearLayout mLinearLayoutTitleAll;

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

        mImageViewLeft = findViewById(R.id.iv_layout_title_base_left);
        mImageViewRight = findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = findViewById(R.id.tv_layout_title_base_middle);
        mLinearLayoutTitleAll = findViewById(R.id.ll_layout_title_base_all);

        this.setTitleBackground(R.color.material_light_blue_a700);
        this.setTitleRightButtonVisibilite(false);
        mImageViewLeft.setImageResource(R.drawable.ic_back);

        mImageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnTitleRightClickListener) {
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

    public interface OnTitleRightClickListener {
        void onClick();
    }

    public void setOnTitleRightClickListener(OnTitleRightClickListener listener) {
        this.mOnTitleRightClickListener = listener;
    }

    //  设置标题右按钮是否可见
    public void setTitleRightButtonVisibilite(boolean visibilite) {
        if (visibilite) {
            mImageViewRight.setVisibility(View.VISIBLE);
            return;
        }
        mImageViewRight.setVisibility(View.GONE);
    }

    //  设置右按钮图标
    public void setTitleRightButtonResource(int resourceid) {
        mImageViewRight.setImageResource(resourceid);
    }

    //  设置标题内容
    public void setTitleText(String title) {
        mTextViewTitle.setText(title);
    }

    //  设置标题栏背景
    public void setTitleBackground(int resouceid) {
        mLinearLayoutTitleAll.setBackgroundResource(resouceid);
    }
}
