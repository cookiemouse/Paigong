package com.tianyigps.xiepeng.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.adapter.HandingAdapter;
import com.tianyigps.xiepeng.data.AdapterHandingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djc on 2017/7/13.
 */

public class HandingFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTextViewHead, mTextViewManager;
    private ListView mListViewHanding;

    private List<AdapterHandingData> mAdapterHandingDataList;
    private HandingAdapter mHandingAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_handing, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    private void init(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_handing);
        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mTextViewHead = view.findViewById(R.id.tv_fragment_handing_head);
        mTextViewManager = view.findViewById(R.id.tv_fragment_handing_manager);

        mListViewHanding = view.findViewById(R.id.lv_fragment_handling);

        mAdapterHandingDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mAdapterHandingDataList.add(new AdapterHandingData("万惠南宁"
                    , "2017-01-02 17:30"
                    , "上海市浦东区东方路985号一百杉杉大厦"
                    , "TY2017010215542001"
                    , "南柱赫"
                    , "1234567890"
                    , 5, 5));
        }

        mHandingAdapter = new HandingAdapter(getContext(), mAdapterHandingDataList);
        mListViewHanding.setAdapter(mHandingAdapter);
    }

    private void setEventListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        mSwipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
//                if (mListView.getFirstVisiblePosition() > 0 || )
//                return false;
                return mListViewHanding.getChildCount() > 0 &&
                        (mListViewHanding.getFirstVisiblePosition() > 0
                                || mListViewHanding.getChildAt(0).getTop() < mListViewHanding.getPaddingTop());
            }
        });
    }
}
