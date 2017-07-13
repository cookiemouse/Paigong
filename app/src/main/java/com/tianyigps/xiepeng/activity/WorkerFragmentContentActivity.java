package com.tianyigps.xiepeng.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.fragment.HandingFragment;
import com.tianyigps.xiepeng.fragment.OrderFragment;

public class WorkerFragmentContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WorkerContentActivity";

    private FrameLayout mFrameLayoutContent;

    //  底部控件
    private LinearLayout mLinearLayoutOrder, mLinearLayoutHandling, mLinearLayoutHistory, mLinearLayoutMine;
    private ImageView mImageViewOrder, mImageViewHandling, mImageViewHistory, mImageViewMine;
    private TextView mTextViewOrder, mTextViewHandling, mTextViewHistory, mTextViewMine;

    private OrderFragment mOrderFragment;
    private HandingFragment mHandingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_fragment_content);

        //  透明状态栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //  透明标题栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        init();

        setEventListener();
    }

    @Override
    public void onClick(View view) {
        resetBottomView();
        switch (view.getId()) {
            case R.id.ll_fragment_content_bottom_order: {
                mImageViewOrder.setImageResource(R.drawable.ic_tab_task_selected);
                mTextViewOrder.setTextColor(getResources().getColor(R.color.colorTextSelect));

                showFragment(mOrderFragment);
                break;
            }
            case R.id.ll_fragment_content_bottom_handling: {
                mImageViewHandling.setImageResource(R.drawable.ic_tab_doing_selected);
                mTextViewHandling.setTextColor(getResources().getColor(R.color.colorTextSelect));

                showFragment(mHandingFragment);
                break;
            }
            case R.id.ll_fragment_content_bottom_history: {
                mImageViewHistory.setImageResource(R.drawable.ic_tab_history_selected);
                mTextViewHistory.setTextColor(getResources().getColor(R.color.colorTextSelect));
                break;
            }
            case R.id.ll_fragment_content_bottom_mine: {
                mImageViewMine.setImageResource(R.drawable.ic_tab_mine_selected);
                mTextViewMine.setTextColor(getResources().getColor(R.color.colorTextSelect));
                break;
            }
            default: {
                Log.i(TAG, "onClick: default");
            }
        }
    }

    //  初始化
    private void init() {
        mLinearLayoutOrder = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_order);
        mLinearLayoutHandling = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_handling);
        mLinearLayoutHistory = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_history);
        mLinearLayoutMine = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_mine);

        mImageViewOrder = (ImageView) findViewById(R.id.iv_fragment_content_bottom_order);
        mImageViewHandling = (ImageView) findViewById(R.id.iv_fragment_content_bottom_handling);
        mImageViewHistory = (ImageView) findViewById(R.id.iv_fragment_content_bottom_history);
        mImageViewMine = (ImageView) findViewById(R.id.iv_fragment_content_bottom_mine);

        mTextViewOrder = (TextView) findViewById(R.id.tv_fragment_content_bottom_order);
        mTextViewHandling = (TextView) findViewById(R.id.tv_fragment_content_bottom_handling);
        mTextViewHistory = (TextView) findViewById(R.id.tv_fragment_content_bottom_history);
        mTextViewMine = (TextView) findViewById(R.id.tv_fragment_content_bottom_mine);

        //  底部按钮标记第一个
        mImageViewOrder.setImageResource(R.drawable.ic_tab_task_selected);
        mTextViewOrder.setTextColor(getResources().getColor(R.color.colorTextSelect));

        mOrderFragment = new OrderFragment();
        mHandingFragment = new HandingFragment();

        showFragment(mOrderFragment);
    }

    //  设置事件监听
    private void setEventListener() {
        mLinearLayoutOrder.setOnClickListener(this);
        mLinearLayoutHandling.setOnClickListener(this);
        mLinearLayoutHistory.setOnClickListener(this);
        mLinearLayoutMine.setOnClickListener(this);
    }

    //  底部按钮复位
    private void resetBottomView() {
        mImageViewOrder.setImageResource(R.drawable.ic_tab_task);
        mImageViewHandling.setImageResource(R.drawable.ic_tab_doing);
        mImageViewHistory.setImageResource(R.drawable.ic_tab_history);
        mImageViewMine.setImageResource(R.drawable.ic_tab_mine);

        mTextViewOrder.setTextColor(getResources().getColor(R.color.colorText));
        mTextViewHandling.setTextColor(getResources().getColor(R.color.colorText));
        mTextViewHistory.setTextColor(getResources().getColor(R.color.colorText));
        mTextViewMine.setTextColor(getResources().getColor(R.color.colorText));
    }

    //  变化Fragmnet
    private void showFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            fragmentTransaction.hide(fragment);
        }
        if (frag.isAdded()) {
            fragmentTransaction.show(frag);
        } else {
            fragmentTransaction.add(R.id.fl_activity_worker_content, frag);
        }
        fragmentTransaction.commit();
    }
}
