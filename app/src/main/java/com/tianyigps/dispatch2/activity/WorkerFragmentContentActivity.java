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
import com.tianyigps.dispatch2.fragment.HandingFragment;
import com.tianyigps.dispatch2.fragment.HandledFragment;
import com.tianyigps.dispatch2.fragment.MineFragment;
import com.tianyigps.dispatch2.fragment.OrderFragment;
import com.tianyigps.dispatch2.interfaces.OnGetWorkerOrderHandingListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.BitmapU;

import q.rorbin.badgeview.QBadgeView;

public class WorkerFragmentContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WorkerContentActivity";

    private FrameLayout mFrameLayoutContent;

    private ImageView mImageViewBackground;
    private Bitmap mBitmap;

    //  顶部小红点锚点
    private View mViewRedDot, mViewRedDotOrder, mViewRedDotHanding;
    private QBadgeView mQBadgeView, mQBadgeViewOrder, mQBadgeViewHanding;

    //  底部控件
    private LinearLayout mLinearLayoutOrder, mLinearLayoutHandling, mLinearLayoutHistory, mLinearLayoutMine;
    private ImageView mImageViewOrder, mImageViewHandling, mImageViewHistory, mImageViewMine;
    private TextView mTextViewOrder, mTextViewHandling, mTextViewHistory, mTextViewMine;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int launchMode;

    private OrderFragment mOrderFragment;
    private HandingFragment mHandingFragment;
    private HandledFragment mHandledFragment;
    private MineFragment mMineFragment;

    //  广播接收器
    private BroadcastReceiver mBroadcastReceiver;
    private Intent mIntentReceiver;

    //  NetWorkerManager
    private NetworkManager mNetworkManager;
    private String mToken, mUserName;
    private int mEid, mCount = 0;
    private MyHandler myHandler;

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
    protected void onResume() {
        super.onResume();
        mSharedpreferenceManager.saveUiMode(Data.DATA_LAUNCH_MODE_WORKER);
        mNetworkManager.getWorkerOrderHanding(mEid, mToken, mUserName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mIntentReceiver) {
            unregisterReceiver(mBroadcastReceiver);
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

                showFragment(mHandledFragment);
                break;
            }
            case R.id.ll_fragment_content_bottom_mine: {
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
                WorkerFragmentContentActivity.this.finish();
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
        mLinearLayoutOrder = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_order);
        mLinearLayoutHandling = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_handling);
        mLinearLayoutHistory = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_history);
        mLinearLayoutMine = (LinearLayout) findViewById(R.id.ll_fragment_content_bottom_mine);

        mViewRedDot = findViewById(R.id.view_worker_red_dot);
        mViewRedDotOrder = findViewById(R.id.view_worker_red_dot_order);
        mViewRedDotHanding = findViewById(R.id.view_worker_red_dot_handing);

        mImageViewBackground = (ImageView) findViewById(R.id.iv_activity_worker_fragment_content);
        mBitmap = BitmapU.getBitmap(this, R.drawable.bg_content);
        mImageViewBackground.setImageBitmap(mBitmap);

        mImageViewOrder = (ImageView) findViewById(R.id.iv_fragment_content_bottom_order);
        mImageViewHandling = (ImageView) findViewById(R.id.iv_fragment_content_bottom_handling);
        mImageViewHistory = (ImageView) findViewById(R.id.iv_fragment_content_bottom_history);
        mImageViewMine = (ImageView) findViewById(R.id.iv_fragment_content_bottom_mine);

        mTextViewOrder = (TextView) findViewById(R.id.tv_fragment_content_bottom_order);
        mTextViewHandling = (TextView) findViewById(R.id.tv_fragment_content_bottom_handling);
        mTextViewHistory = (TextView) findViewById(R.id.tv_fragment_content_bottom_history);
        mTextViewMine = (TextView) findViewById(R.id.tv_fragment_content_bottom_mine);

        mSharedpreferenceManager = new SharedpreferenceManager(this);
        mToken = mSharedpreferenceManager.getToken();
        mUserName = mSharedpreferenceManager.getAccount();
        mEid = mSharedpreferenceManager.getEid();
        launchMode = mSharedpreferenceManager.getLaunchMode();

        mNetworkManager = new NetworkManager();

        myHandler = new MyHandler();

        mOrderFragment = new OrderFragment();
        mHandingFragment = new HandingFragment();
        mHandledFragment = new HandledFragment();
        mMineFragment = new MineFragment();

        Intent intent = getIntent();
        int showFragment = intent.getIntExtra(Data.DATA_INTENT_WORKER_FRAGMENT, 0);
        switch (showFragment) {
            case 0: {
                break;
            }
            case Data.DATA_INTENT_WORKER_FRAGMENT_HANDING: {
                showHandingFragment();
                return;
            }
            case Data.DATA_INTENT_WORKER_FRAGMENT_HANDED: {
                showHandFragment();
                return;
            }
            case 3: {
                //  mine fragment
                return;
            }
            default: {
                Log.i(TAG, "init: default-->" + showFragment);
            }
        }

        //  底部按钮标记第一个
        mImageViewOrder.setImageResource(R.drawable.ic_tab_task_selected);
        mTextViewOrder.setTextColor(getResources().getColor(R.color.colorTextSelect));
        showFragment(mOrderFragment);

        mQBadgeView = new QBadgeView(this);
        mQBadgeViewOrder = new QBadgeView(this);
        mQBadgeViewHanding = new QBadgeView(this);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: ");
                if (launchMode != Data.DATA_LAUNCH_MODE_WORKER) {
                    showRedDot();
                }
                showRedDotOnOrder(true);
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Data.BROAD_FILTER);
        intentFilter.addCategory(Data.BROAD_CATEGORY);
        if (null != mIntentReceiver) {
            unregisterReceiver(mBroadcastReceiver);
        }
        mIntentReceiver = registerReceiver(mBroadcastReceiver, intentFilter);
    }

    //  设置事件监听
    private void setEventListener() {
        mLinearLayoutOrder.setOnClickListener(this);
        mLinearLayoutHandling.setOnClickListener(this);
        mLinearLayoutHistory.setOnClickListener(this);
        mLinearLayoutMine.setOnClickListener(this);

        mNetworkManager.setGetWorkerOrderHandingListener(new OnGetWorkerOrderHandingListener() {
            @Override
            public void onFailure() {
                //  do nothing
            }

            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WorkerHandingBean workerHandingBean = gson.fromJson(result, WorkerHandingBean.class);
                if (!workerHandingBean.isSuccess()) {
                    return;
                }
                mCount = workerHandingBean.getObj().size();
                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
    }

    //  显示小红点
    private void showRedDot() {
        if (null == mQBadgeView) {
            mQBadgeView = new QBadgeView(this);
        }
        mQBadgeView.bindTarget(mViewRedDot).setBadgeNumber(-1);
    }

    //  显示小红点
    public void showRedDotOnOrder(boolean show) {
        if (null == mQBadgeViewOrder) {
            mQBadgeViewOrder = new QBadgeView(this);
        }
        if (show) {
            mQBadgeViewOrder.bindTarget(mViewRedDotOrder).setBadgeNumber(-1);
        } else {
            mQBadgeViewOrder.bindTarget(mViewRedDotOrder).setBadgeNumber(0);
        }
    }

    //  显示小红点
    private void showRedDotOnHanding(boolean show) {
        if (null == mQBadgeViewHanding) {
            mQBadgeViewHanding = new QBadgeView(this);
        }
        if (show) {
            mQBadgeViewHanding.bindTarget(mViewRedDotHanding).setBadgeNumber(-1);
        } else {
            mQBadgeViewHanding.bindTarget(mViewRedDotHanding).setBadgeNumber(0);
        }
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

    //  显示进行中Fragment
    public void showHandingFragment() {
        resetBottomView();
        mImageViewHandling.setImageResource(R.drawable.ic_tab_doing_selected);
        mTextViewHandling.setTextColor(getResources().getColor(R.color.colorTextSelect));

        showFragment(mHandingFragment);
    }

    //  显示完成订单Fragment
    public void showHandFragment() {
        resetBottomView();
        mImageViewHistory.setImageResource(R.drawable.ic_tab_history_selected);
        mTextViewHistory.setTextColor(getResources().getColor(R.color.colorTextSelect));

        showFragment(mHandledFragment);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_1: {
                    if (mCount > 0) {
                        showRedDotOnHanding(true);
                    } else {
                        showRedDotOnHanding(false);
                    }
                    break;
                }
            }
        }
    }
}
