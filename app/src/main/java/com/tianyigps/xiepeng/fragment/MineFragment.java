package com.tianyigps.xiepeng.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.activity.ChoiceWorkerActivity;
import com.tianyigps.xiepeng.activity.ManagerFragmentContentActivity;
import com.tianyigps.xiepeng.activity.ModifyPasswordActivity;
import com.tianyigps.xiepeng.activity.OrderTrackActivity;
import com.tianyigps.xiepeng.activity.StatisticsActivity;
import com.tianyigps.xiepeng.activity.WorkerFragmentContentActivity;
import com.tianyigps.xiepeng.adapter.MineAdapter;
import com.tianyigps.xiepeng.data.AdapterMineData;
import com.tianyigps.xiepeng.data.Data;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class MineFragment extends Fragment {

    private static final String TAG = "MineFragment";

    private ListView mListViewMine;
    private List<AdapterMineData> mAdapterMineDataList;
    private MineAdapter mMineAdapter;

    //  标题栏
    private LinearLayout mLinearLayoutTitle;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private TextView mTextViewExit;

    private SharedpreferenceManager mSharedpreferenceManager;
    private int uiMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_mine, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    private void init(View view) {
        mLinearLayoutTitle = view.findViewById(R.id.ll_layout_title_base_all);
        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mListViewMine = view.findViewById(R.id.lv_fragment_mine);
        mTextViewExit = view.findViewById(R.id.tv_fragment_mine);

        mAdapterMineDataList = new ArrayList<>();
        mAdapterMineDataList.add(new AdapterMineData(R.drawable.ic_statistics, "质量统计"));
        mAdapterMineDataList.add(new AdapterMineData(R.drawable.ic_modify_password, "修改密码"));

        mAdapterMineDataList.add(new AdapterMineData(R.drawable.ic_modify_password, "测试选择工程师"));
        mAdapterMineDataList.add(new AdapterMineData(R.drawable.ic_modify_password, "测试订单跟踪"));

        mMineAdapter = new MineAdapter(getContext(), mAdapterMineDataList);

        mListViewMine.setAdapter(mMineAdapter);

        mSharedpreferenceManager = new SharedpreferenceManager(getContext());
        uiMode = mSharedpreferenceManager.getUiMode();
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.mine);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setVisibility(View.GONE);
        mLinearLayoutTitle.setBackgroundResource(R.color.colorBlueTheme);
    }

    private void setEventListener() {

        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: getActivity-->" + getActivity().getCallingActivity());
                Intent intent = new Intent();
                if (Data.DATA_LAUNCH_MODE_WORKER == uiMode) {
                    intent.setClass(getContext(), ManagerFragmentContentActivity.class);
                } else {
                    intent.setClass(getContext(), WorkerFragmentContentActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        mListViewMine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: {
                        //  2017/7/13 质量统计
                        Intent intent = new Intent(getContext(), StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        // 2017/7/13 修改密码
                        Intent intent = new Intent(getContext(), ModifyPasswordActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        // 2017/7/13 修改密码
                        Intent intent = new Intent(getContext(), ChoiceWorkerActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        // 2017/7/13 修改密码
                        Intent intent = new Intent(getContext(), OrderTrackActivity.class);
                        startActivity(intent);
                        break;
                    }
                    default: {
                        Log.i(TAG, "onItemClick: default");
                    }
                }
            }
        });

        mTextViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/13 退出登陆
                showExitDialog();
            }
        });
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("是否要退出登陆！");
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exitLogin();
                getActivity().finish();
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

    //  退出登陆，数据清零
    private void exitLogin() {
        mSharedpreferenceManager.saveUserData(0, "", "", "", "", "", 1);
    }
}
