package com.tianyigps.xiepeng.fragment;

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
import com.tianyigps.xiepeng.activity.CustomSignActivity;
import com.tianyigps.xiepeng.activity.EditRemarksActivity;
import com.tianyigps.xiepeng.activity.ModifyPasswordActivity;
import com.tianyigps.xiepeng.activity.StatisticsActivity;
import com.tianyigps.xiepeng.adapter.MineAdapter;
import com.tianyigps.xiepeng.data.AdapterMineData;
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

        // TODO: 2017/7/20 测试
        mAdapterMineDataList.add(new AdapterMineData(R.drawable.ic_modify_password, "测试客户签字"));
        mAdapterMineDataList.add(new AdapterMineData(R.drawable.ic_modify_password, "测试填写备注"));

        mMineAdapter = new MineAdapter(getContext(), mAdapterMineDataList);

        mListViewMine.setAdapter(mMineAdapter);

        mSharedpreferenceManager = new SharedpreferenceManager(getContext());
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
                // TODO: 2017/7/13 切换用户
            }
        });

        mListViewMine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: {
                        // TODO: 2017/7/13 质量统计
                        Intent intent = new Intent(getContext(), StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        // TODO: 2017/7/13 修改密码
//                        Intent intent = new Intent(getContext(), ModifyPasswordActivity.class);
                        Intent intent = new Intent(getContext(), ModifyPasswordActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        //  2017/7/13 测试
//                        Intent intent = new Intent(getContext(), ModifyPasswordActivity.class);
                        Intent intent = new Intent(getContext(), CustomSignActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        //  2017/7/13 测试
//                        Intent intent = new Intent(getContext(), ModifyPasswordActivity.class);
                        Intent intent = new Intent(getContext(), EditRemarksActivity.class);
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
                exitLogin();
                getActivity().finish();
            }
        });
    }

    //  退出登陆，数据清零
    private void exitLogin() {
        mSharedpreferenceManager.saveUserData(0, "", "", "", "", 1);
    }
}