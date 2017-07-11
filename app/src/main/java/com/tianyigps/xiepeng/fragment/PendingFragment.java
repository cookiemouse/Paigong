package com.tianyigps.xiepeng.fragment;

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
import com.tianyigps.xiepeng.adapter.PendingAdapter;
import com.tianyigps.xiepeng.data.AdapterPendingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;
    private ImageView mImageViewSearch;
    private EditText mEditTextSearch;
    private ListView mListView;

    private List<AdapterPendingData> mAdapterPendingDataList;
    private PendingAdapter mPendingAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = inflater.inflate(R.layout.fragment_pending, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    private void init(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_pending);
        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_manager_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_manager_right);
        mImageViewSearch = view.findViewById(R.id.iv_fragment_search);
        mEditTextSearch = view.findViewById(R.id.et_layout_search);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_manager_middle);
        mListView = view.findViewById(R.id.lv_fragment_pending);

        mEditTextSearch.clearFocus();
        mEditTextSearch.setCursorVisible(false);

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterPendingDataList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mAdapterPendingDataList.add(new AdapterPendingData("order", "name", "phoneNumber", "time"
                    , "address", "orderType", i, 2));
        }

        mPendingAdapter = new PendingAdapter(getContext(), mAdapterPendingDataList);

        mListView.setAdapter(mPendingAdapter);
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
    }
}
