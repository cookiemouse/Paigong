package com.tianyigps.dispatch2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.fragment.HandingFragment;
import com.tianyigps.dispatch2.fragment.HandledFragment;
import com.tianyigps.dispatch2.fragment.MineFragment;
import com.tianyigps.dispatch2.fragment.OrderFragment;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.BitmapU;

public class WorkerFragmentContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WorkerContentActivity";

    private FrameLayout mFrameLayoutContent;

    private ImageView mImageViewBackground;
    private Bitmap mBitmap;

    //  底部控件
    private LinearLayout mLinearLayoutOrder, mLinearLayoutHandling, mLinearLayoutHistory, mLinearLayoutMine;
    private ImageView mImageViewOrder, mImageViewHandling, mImageViewHistory, mImageViewMine;
    private TextView mTextViewOrder, mTextViewHandling, mTextViewHistory, mTextViewMine;

    private SharedpreferenceManager mSharedpreferenceManager;

    private OrderFragment mOrderFragment;
    private HandingFragment mHandingFragment;
    private HandledFragment mHandledFragment;
    private MineFragment mMineFragment;

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
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
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

        //  底部按钮标记第一个
        mImageViewOrder.setImageResource(R.drawable.ic_tab_task_selected);
        mTextViewOrder.setTextColor(getResources().getColor(R.color.colorTextSelect));

        mOrderFragment = new OrderFragment();
        mHandingFragment = new HandingFragment();
        mHandledFragment = new HandledFragment();
        mMineFragment = new MineFragment();

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

    //  显示进行中Fragment
    public void showHandingFragment() {
        resetBottomView();
        mImageViewHandling.setImageResource(R.drawable.ic_tab_doing_selected);
        mTextViewHandling.setTextColor(getResources().getColor(R.color.colorTextSelect));

        showFragment(mHandingFragment);
    }
}
