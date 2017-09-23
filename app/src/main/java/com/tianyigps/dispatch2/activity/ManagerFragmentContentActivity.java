package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.bean.WorkerHandingBean;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.fragment.MineFragment;
import com.tianyigps.dispatch2.fragment.PendedFragment;
import com.tianyigps.dispatch2.fragment.PendingFragment;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderHandingListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.BitmapU;

import q.rorbin.badgeview.QBadgeView;

public class ManagerFragmentContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ManagerContentActivity";

    private FrameLayout mFrameLayout;

    private ImageView mImageViewBackground;
    private Bitmap mBitmap;

    //  顶部小红点锚点
    private View mViewRedDot;
    private QBadgeView mQBadgeView;

    //  底部控件
    private LinearLayout mLinearLayoutPending, mLinearLayoutHandled, mLinearLayoutMine;
    private ImageView mImageViewPending, mImageViewHandled, mImageViewMine;
    private TextView mTextViewPending, mTextViewHandled, mTextViewMine;

    private SharedpreferenceManager mSharedpreferenceManager;

    //  Fragment
    private PendingFragment mPendingFragment;
    private PendedFragment mPendedFragment;
    private MineFragment mMineFragment;

    //  广播接收器
    private BroadcastReceiver mBroadcastReceiver;
    private Intent mIntentReceiver;

    //  是否有进行中的订单
    private NetworkManager mNetworkManager;
    private int mEid;
    private String mToken, mUserName;
    private int mHandingCount = 0;

    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_fragment_content);

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
    protected void onResume() {
        super.onResume();
        mSharedpreferenceManager.saveUiMode(Data.DATA_LAUNCH_MODE_MANAGER);
        mNetworkManager.getWorkerOrderHanding(mEid, mToken, mUserName);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Data.BROAD_FILTER);
        intentFilter.addCategory(Data.BROAD_CATEGORY);
        mIntentReceiver = registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mIntentReceiver) {
            unregisterReceiver(mBroadcastReceiver);
            mIntentReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        if (null != mBitmap) {
            mBitmap.recycle();
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        resetBottomView();
        switch (view.getId()) {
            case R.id.ll_manager_fragment_content_bottom_pending: {
                mImageViewPending.setImageResource(R.drawable.ic_tab_task_selected);
                mTextViewPending.setTextColor(getResources().getColor(R.color.colorTextSelect));

                showFragment(mPendingFragment);
                break;
            }
            case R.id.ll_manager_fragment_content_bottom_handled: {
                mImageViewHandled.setImageResource(R.drawable.ic_tab_history_selected);
                mTextViewHandled.setTextColor(getResources().getColor(R.color.colorTextSelect));

                showFragment(mPendedFragment);
                break;
            }
            case R.id.ll_manager_fragment_content_bottom_mine: {
                mImageViewMine.setImageResource(R.drawable.ic_tab_mine_selected);
                mTextViewMine.setTextColor(getResources().getColor(R.color.colorTextSelect));

                showFragment(mMineFragment);
                break;
            }
            default: {
                Log.i(TAG, "onSignClick: default");
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showFinishDialog();
    }

    private void showFinishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否要退出派工！");
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ManagerFragmentContentActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //  初始化
    private void init() {
        mFrameLayout = (FrameLayout) findViewById(R.id.fl_activity_manager_content);

        mViewRedDot = findViewById(R.id.view_manager_red_dot);
        mQBadgeView = new QBadgeView(this);
        mQBadgeView.bindTarget(mViewRedDot);

        mImageViewBackground = (ImageView) findViewById(R.id.iv_activity_manager_fragment_content);
        mBitmap = BitmapU.getBitmap(this, R.drawable.bg_content);
        mImageViewBackground.setImageBitmap(mBitmap);

        mLinearLayoutPending = (LinearLayout) findViewById(R.id.ll_manager_fragment_content_bottom_pending);
        mLinearLayoutHandled = (LinearLayout) findViewById(R.id.ll_manager_fragment_content_bottom_handled);
        mLinearLayoutMine = (LinearLayout) findViewById(R.id.ll_manager_fragment_content_bottom_mine);

        mImageViewPending = (ImageView) findViewById(R.id.iv_manager_fragment_content_bottom_pending);
        mImageViewHandled = (ImageView) findViewById(R.id.iv_manager_fragment_content_bottom_handled);
        mImageViewMine = (ImageView) findViewById(R.id.iv_manager_fragment_content_bottom_mine);

        mTextViewPending = (TextView) findViewById(R.id.tv_manager_fragment_content_bottom_pending);
        mTextViewHandled = (TextView) findViewById(R.id.tv_manager_fragment_content_bottom_handled);
        mTextViewMine = (TextView) findViewById(R.id.tv_manager_fragment_content_bottom_mine);

        mSharedpreferenceManager = new SharedpreferenceManager(this);

        //  底部按钮标记第一个
        mImageViewPending.setImageResource(R.drawable.ic_tab_task_selected);
        mTextViewPending.setTextColor(getResources().getColor(R.color.colorTextSelect));

        mPendingFragment = new PendingFragment();
        mPendedFragment = new PendedFragment();
        mMineFragment = new MineFragment();
        showFragment(mPendingFragment);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mNetworkManager = new NetworkManager();
        mEid = mSharedpreferenceManager.getEid();
        mToken = mSharedpreferenceManager.getToken();
        mUserName = mSharedpreferenceManager.getAccount();

        myHandler = new MyHandler();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: ");
                showRedDot();
            }
        };
    }

    //  显示小红点
    private void showRedDot() {
        mQBadgeView.setBadgeNumber(-1);
    }

    //  设置事件监听
    private void setEventListener() {
        mLinearLayoutPending.setOnClickListener(this);
        mLinearLayoutHandled.setOnClickListener(this);
        mLinearLayoutMine.setOnClickListener(this);

        mNetworkManager.setGetWorkerOrderHandingListener(new OnGetWorkerOrderHandingListener() {
            @Override
            public void onFailure() {
                // do nothing
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WorkerHandingBean workerHandingBean = gson.fromJson(result, WorkerHandingBean.class);
                if (!workerHandingBean.isSuccess()) {
                    return;
                }
                mHandingCount = workerHandingBean.getObj().size();

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    //  底部按钮复位
    private void resetBottomView() {
        mImageViewPending.setImageResource(R.drawable.ic_tab_task);
        mImageViewHandled.setImageResource(R.drawable.ic_tab_history);
        mImageViewMine.setImageResource(R.drawable.ic_tab_mine);

        mTextViewPending.setTextColor(getResources().getColor(R.color.colorText));
        mTextViewHandled.setTextColor(getResources().getColor(R.color.colorText));
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
            fragmentTransaction.add(R.id.fl_activity_manager_content, frag);
        }
        fragmentTransaction.commit();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_1: {
                    if (mHandingCount > 0) {
                        showRedDot();
                    }
                    break;
                }
            }
        }
    }
}
