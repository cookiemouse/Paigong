package com.tianyigps.xiepeng.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.fragment.MineFragment;
import com.tianyigps.xiepeng.fragment.PendedFragment;
import com.tianyigps.xiepeng.fragment.PendingFragment;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

public class ManagerFragmentContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ManagerContentActivity";

    private FrameLayout mFrameLayout;

    //  底部控件
    private LinearLayout mLinearLayoutPending, mLinearLayoutHandled, mLinearLayoutMine;
    private ImageView mImageViewPending, mImageViewHandled, mImageViewMine;
    private TextView mTextViewPending, mTextViewHandled, mTextViewMine;

    private SharedpreferenceManager mSharedpreferenceManager;

    //  Fragment
    private PendingFragment mPendingFragment;
    private PendedFragment mPendedFragment;
    private MineFragment mMineFragment;

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
    }

    //  设置事件监听
    private void setEventListener() {
        mLinearLayoutPending.setOnClickListener(this);
        mLinearLayoutHandled.setOnClickListener(this);
        mLinearLayoutMine.setOnClickListener(this);
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
}
