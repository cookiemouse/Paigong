package com.tianyigps.xiepeng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tianyigps.xiepeng.R;
import com.tianyigps.xiepeng.activity.WorkerFragmentContentActivity;
import com.tianyigps.xiepeng.adapter.PendingAdapter;
import com.tianyigps.xiepeng.adapter.PopupAdapter;
import com.tianyigps.xiepeng.data.AdapterPendingData;
import com.tianyigps.xiepeng.data.AdapterPopupData;
import com.tianyigps.xiepeng.interfaces.OnPendingOrderListener;
import com.tianyigps.xiepeng.manager.NetworkManager;
import com.tianyigps.xiepeng.manager.SharedpreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by djc on 2017/7/11.
 */

public class PendingFragment extends Fragment {

    private static final String TAG = "PendingFragment";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewSearch;
    private EditText mEditTextSearch;
    private ListView mListView;

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private List<AdapterPendingData> mAdapterPendingDataList;
    private PendingAdapter mPendingAdapter;

    private SharedpreferenceManager mSharedpreferenceManager;
    private NetworkManager mNetworkManager;
    private String jobNo;
    private String token;
    private String userName;

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
        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_pending);
        mImageViewSearch = view.findViewById(R.id.iv_layout_search);
        mEditTextSearch = view.findViewById(R.id.et_layout_search);
        mListView = view.findViewById(R.id.lv_fragment_pending);

        mEditTextSearch.clearFocus();

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterPendingDataList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mAdapterPendingDataList.add(new AdapterPendingData("order", "name", "phoneNumber", "time"
                    , "address", "orderType", i, 2));
        }

        mPendingAdapter = new PendingAdapter(getContext(), mAdapterPendingDataList);

        mListView.setAdapter(mPendingAdapter);

        mSharedpreferenceManager = new SharedpreferenceManager(getContext());
        mNetworkManager = new NetworkManager();

        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mNetworkManager.getPenddingOrder(jobNo, token, "", "",userName);
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.pending);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setImageResource(R.drawable.ic_popup_window);
    }

    private void setEventListener() {
        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WorkerFragmentContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        mImageViewTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mNetworkManager.getPenddingOrder(jobNo, token, "", "", userName);

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

        mNetworkManager.setOnPendingOrderListener(new OnPendingOrderListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
            }
        });
    }

    //显示popupWindow
    private void showPopupWindow() {

        View viewPop = LayoutInflater.from(getContext()).inflate(R.layout.view_popup_window, null);

        ListView listView = viewPop.findViewById(R.id.lv_view_popup);

        final PopupWindow popupWindow = new PopupWindow(viewPop
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , true);

        List<AdapterPopupData> adapterPopupDataList = new ArrayList<>();
        adapterPopupDataList.add(new AdapterPopupData("待派单", 12));
        adapterPopupDataList.add(new AdapterPopupData("安装退回", 2));
        adapterPopupDataList.add(new AdapterPopupData("待审核", 12));
        PopupAdapter popupAdapter = new PopupAdapter(getContext(), adapterPopupDataList);
        listView.setAdapter(popupAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();
            }
        });

        setBackgroundAlpha(0.5f);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });

        popupWindow.showAsDropDown(mImageViewTitleRight);
    }

    //  显示popup时背景的改变
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }
}
