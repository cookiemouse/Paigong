package com.tianyigps.xiepeng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.activity.LocateActivity;
import com.tianyigps.xiepeng.adapter.OrderAdapter;
import com.tianyigps.xiepeng.data.AdapterOrderData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djc on 2017/7/11.
 */

public class OrderFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewTitleRight;
    private TextView mTextViewTitle;
    private ImageView mImageViewSearch;
    private EditText mEditTextSearch;
    private ListView mListView;

    private List<AdapterOrderData> mAdapterOrderDataList;
    private OrderAdapter mOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_worker_order, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    private void init(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_worker_order);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_worker_right);
        mImageViewSearch = view.findViewById(R.id.iv_layout_search);
        mEditTextSearch = view.findViewById(R.id.et_layout_search);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_worker_middle);
        mListView = view.findViewById(R.id.lv_fragment_worker_order);

        mEditTextSearch.clearFocus();

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterOrderDataList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mAdapterOrderDataList.add(new AdapterOrderData("万惠南宁"
                    , "2017-01-02 17:30"
                    , "上海市浦东区东方路985号一百杉杉大厦"
                    , "TY2017010215542001"
                    , "南柱赫"
                    , "1234567890"
                    , "orderType"
                    , i, 2));
        }

        mOrderAdapter = new OrderAdapter(getContext(), mAdapterOrderDataList);

        mListView.setAdapter(mOrderAdapter);
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
                return mListView.getChildCount() > 0 &&
                        (mListView.getFirstVisiblePosition() > 0
                                || mListView.getChildAt(0).getTop() < mListView.getPaddingTop());
            }
        });

        mImageViewTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LocateActivity.class);
                startActivity(intent);
            }
        });
    }
}
