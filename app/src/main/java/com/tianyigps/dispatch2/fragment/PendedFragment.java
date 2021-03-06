package com.tianyigps.dispatch2.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.activity.PendDetailsActivity;
import com.tianyigps.dispatch2.activity.WorkerFragmentContentActivity;
import com.tianyigps.dispatch2.adapter.PendedAdapter;
import com.tianyigps.dispatch2.adapter.PopupAdapter;
import com.tianyigps.dispatch2.bean.NumberBean;
import com.tianyigps.dispatch2.bean.PendedBean;
import com.tianyigps.dispatch2.data.AdapterPendedData;
import com.tianyigps.dispatch2.data.AdapterPopupData;
import com.tianyigps.dispatch2.data.Data;
import com.tianyigps.dispatch2.dialog.ChoiceMapDialogFragment;
import com.tianyigps.dispatch2.interfaces.OnPendedListener;
import com.tianyigps.dispatch2.interfaces.OnPendedNumListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;
import com.tianyigps.dispatch2.utils.ChoiceMapU;
import com.tianyigps.dispatch2.utils.RegularU;

import java.util.ArrayList;
import java.util.List;

import static com.tianyigps.dispatch2.data.Data.MSG_3;

/**
 * Created by djc on 2017/7/11.
 */

public class PendedFragment extends Fragment {

    private static final String TAG = "PendedFragment";

    private static final int DELAY_GONE = 1000;
    private static final int DELAY_LAST = 2000;
    private static final int DELAY_ERROR = 3000;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mImageViewSearch, mImageViewDelete;
    private EditText mEditTextSearch;
    private TextView mTextViewSearchNull;
    private ListView mListView;

    //  无数据时显示UI
    private LinearLayout mLinearLayoutDefault;

    //  标题栏
    private ImageView mImageViewTitleLeft, mImageViewTitleRight;
    private TextView mTextViewTitle;

    private List<AdapterPendedData> mAdapterPendedDataList;
    private PendedAdapter mPendedAdapter;

    private NetworkManager mNetworkManager;
    private SharedpreferenceManager mSharedpreferenceManager;
    private MyHandler myHandler;
    private String jobNo;
    private String token;
    private String userName;
    private String mStringMessage;

    private String mKey = "";

    //  加载更多
    private View mViewMore;
    private TextView mTextViewMore;
    private ProgressBar mProgressBarMore;
    private boolean addAble = true, isLast = false;

    //  popup
    private List<AdapterPopupData> mAdapterPopupDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View viewRoot = inflater.inflate(R.layout.fragment_pended, container, false);

        init(viewRoot);

        setEventListener();

        return viewRoot;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mEditTextSearch.setText(null);
            mImageViewDelete.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);
            mTextViewSearchNull.setVisibility(View.GONE);
            mNetworkManager.getPended(jobNo, token, "", "", "", userName);
        }
    }

    private void init(View view) {
        mImageViewTitleLeft = view.findViewById(R.id.iv_layout_title_base_left);
        mImageViewTitleRight = view.findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = view.findViewById(R.id.tv_layout_title_base_middle);
        initTitle();

        mNetworkManager = new NetworkManager();
        myHandler = new MyHandler();
        mSharedpreferenceManager = new SharedpreferenceManager(getContext());
        jobNo = mSharedpreferenceManager.getJobNo();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();

        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_pended);
        mImageViewSearch = view.findViewById(R.id.iv_layout_search);
        mImageViewDelete = view.findViewById(R.id.iv_layout_search_delete);
        mEditTextSearch = view.findViewById(R.id.et_layout_search);
        mListView = view.findViewById(R.id.lv_fragment_pended);
        mLinearLayoutDefault = view.findViewById(R.id.ll_fragment_pended_default);
        mTextViewSearchNull = view.findViewById(R.id.tv_fragment_pended_default);

        mViewMore = LayoutInflater.from(getContext()).inflate(R.layout.layout_load_more, null);
        mTextViewMore = mViewMore.findViewById(R.id.tv_layout_load_more);
        mProgressBarMore = mViewMore.findViewById(R.id.pb_layout_load_more);

        mEditTextSearch.clearFocus();

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mAdapterPendedDataList = new ArrayList<>();
        mAdapterPopupDataList = new ArrayList<>();

        mPendedAdapter = new PendedAdapter(getContext(), mAdapterPendedDataList);

        mListView.addFooterView(mViewMore);

        mListView.setAdapter(mPendedAdapter);

        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkManager.getPended(jobNo, token, "", "", "", userName);
    }

    private void initTitle() {
        mTextViewTitle.setText(R.string.handled);
        mImageViewTitleLeft.setImageResource(R.drawable.ic_switch_account);
        mImageViewTitleRight.setImageResource(R.drawable.ic_popup_window);
    }

    private void setEventListener() {
        mTextViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.smoothScrollToPosition(0);
            }
        });

        mImageViewTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WorkerFragmentContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                getActivity().finish();
            }
        });

        mImageViewTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPendedNumber();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mKey = "";
                mEditTextSearch.setText(null);
                mImageViewDelete.setVisibility(View.GONE);
                mNetworkManager.getPended(jobNo, token, "", "", "", userName);
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

        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKey = mEditTextSearch.getText().toString();
                mKey = mKey.trim();
                mSwipeRefreshLayout.setRefreshing(true);
                mNetworkManager.getPended(jobNo, token, "", mKey, "", userName);

                if (!"".equals(mKey)) {
                    mImageViewDelete.setVisibility(View.VISIBLE);
                }
            }
        });

        mImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextSearch.setText(null);
                mSwipeRefreshLayout.setRefreshing(true);
                mNetworkManager.getPended(jobNo, token, "", "", "", userName);

                mImageViewDelete.setVisibility(View.GONE);
            }
        });

        mPendedAdapter.setOnItemListener(new PendedAdapter.OnItemListener() {
            @Override
            public void onMap(int position) {
                String address = mAdapterPendedDataList.get(position).getAddress();
                if (!ChoiceMapU.toMap(getContext(), address)) {
                    ChoiceMapDialogFragment mChoiceMapDialogFragment = new ChoiceMapDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Data.DATA_INTENT_ADDRESS, address);
                    mChoiceMapDialogFragment.setArguments(bundle);
                    mChoiceMapDialogFragment.show(getChildFragmentManager(), "ChoiceMapDialog");
                }
            }

            @Override
            public void onContact(int position) {
                AdapterPendedData data = mAdapterPendedDataList.get(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getContactPhone()));
                startActivity(intent);
            }

            @Override
            public void onCall(int position) {
                AdapterPendedData data = mAdapterPendedDataList.get(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getWorkerPhone()));
                startActivity(intent);
            }

            @Override
            public void onItem(int position) {
                Intent intent = new Intent(getContext(), PendDetailsActivity.class);
                AdapterPendedData data = mAdapterPendedDataList.get(position);
                intent.putExtra(Data.DATA_INTENT_ORDER_NO, data.getOrderNo());
                intent.putExtra(Data.DATA_INTENT_ORDER_STATUS, data.getOrderStatus());
                startActivity(intent);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount < 0) {
                    return;
                }
                if (totalItemCount == visibleItemCount) {
                    return;
                }
                if (totalItemCount == (firstVisibleItem + visibleItemCount)) {
                    //  2017/8/2 开始加载
                    if (addAble && !mSwipeRefreshLayout.isRefreshing()) {
                        addAble = false;
                        if (isLast) {
                            mProgressBarMore.setVisibility(View.GONE);
                            mTextViewMore.setVisibility(View.VISIBLE);
                            mTextViewMore.setText("我是有底线的!");
                            myHandler.sendEmptyMessageDelayed(Data.MSG_2, DELAY_LAST);
                            return;
                        }
                        mProgressBarMore.setVisibility(View.VISIBLE);
                        mTextViewMore.setVisibility(View.VISIBLE);
                        mTextViewMore.setText("正在加载...");
                        // 17-8-2 加载下面数据
                        mNetworkManager.getPended(jobNo
                                , token
                                , ""
                                , mKey
                                , "" + mAdapterPendedDataList.get(mAdapterPendedDataList.size() - 1).getOrderId()
                                , userName);
                    }
                }
            }
        });

        mNetworkManager.setOnPendedListener(new OnPendedListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                PendedBean pendedBean = gson.fromJson(result, PendedBean.class);
                if (!pendedBean.isSuccess()) {
                    mStringMessage = pendedBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                isLast = (pendedBean.getObj().size() == 0);
                if (isLast) {
                    myHandler.sendEmptyMessageDelayed(MSG_3, DELAY_LAST);
//                    return;
                }

                if (mSwipeRefreshLayout.isRefreshing()) {
                    mAdapterPendedDataList.clear();
                }
                for (PendedBean.ObjBean objBean : pendedBean.getObj()) {

                    int orderStatus = objBean.getOrderStatus();
                    if (Data.STATUS_2 == orderStatus
                            || Data.STATUS_3 == orderStatus
                            || Data.STATUS_4 == orderStatus
                            || Data.STATUS_5 == orderStatus
                            || Data.STATUS_7 == orderStatus) {

                        String address = objBean.getProvince() + objBean.getCity() + objBean.getDistrict() + objBean.getDetail();
                        String worker;
                        if (RegularU.isEmpty(objBean.getEName())) {
                            worker = "";
                        } else {
                            worker = objBean.getEName();
                        }
                        if (RegularU.isEmpty(objBean.getJobNo())) {
                            worker += "";
                        } else {
                            worker += objBean.getJobNo();
                        }
                        mAdapterPendedDataList.add(new AdapterPendedData(objBean.getCustName()
                                , address
                                , objBean.getJobNo()
                                , worker
                                , objBean.getPhoneNo()
                                , objBean.getContactName()
                                , objBean.getContactPhone()
                                , objBean.getDoorTime()
                                , objBean.getOrderStatus()
                                , objBean.getReviseFlag()
                                , objBean.getOrderNo()
                                , objBean.getOrderId()));
                    }

                }

                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });

        mNetworkManager.setOnPendedNumListener(new OnPendedNumListener() {
            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();

                NumberBean numberBean = gson.fromJson(result, NumberBean.class);
                if (!numberBean.isSuccess()) {
                    mStringMessage = numberBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                mAdapterPopupDataList.clear();

                for (NumberBean.ObjBean objBean : numberBean.getObj()) {
                    mAdapterPopupDataList.add(new AdapterPopupData(objBean.getStatus(), objBean.getNum()));
                }

                myHandler.sendEmptyMessage(Data.MSG_5);
            }
        });
    }

    //  获取popupNumber
    private void getPendedNumber() {
        mNetworkManager.getPendedNum(jobNo, token, userName);
    }

    //显示popupWindow
    private void showPopupWindow() {

        View viewPop = LayoutInflater.from(getContext()).inflate(R.layout.view_popup_window, null);

        ListView listView = viewPop.findViewById(R.id.lv_view_popup);

        final PopupWindow popupWindow = new PopupWindow(viewPop
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT
                , true);

        PopupAdapter popupAdapter = new PopupAdapter(getContext(), mAdapterPopupDataList);
        listView.setAdapter(popupAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mEditTextSearch.setText(null);
                int status = mAdapterPopupDataList.get(i).getOrderStatus();
                mSwipeRefreshLayout.setRefreshing(true);
                mNetworkManager.getPended(jobNo, token, "" + status, "", "", userName);
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

    //  显示信息Dialog
    private void showMessageDialog(String msg) {
        if (null == getContext()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    if (mAdapterPendedDataList.size() == 0 && RegularU.isEmpty(mKey)) {
                        mLinearLayoutDefault.setVisibility(View.VISIBLE);
                    } else if (mAdapterPendedDataList.size() == 0 && !RegularU.isEmpty(mKey)) {
                        mTextViewSearchNull.setVisibility(View.VISIBLE);
                    } else {
                        mTextViewSearchNull.setVisibility(View.GONE);
                        mLinearLayoutDefault.setVisibility(View.GONE);
                    }
                    mPendedAdapter.notifyDataSetChanged();
                    myHandler.sendEmptyMessage(Data.MSG_2);
                    break;
                }
                case Data.MSG_2: {
                    //  加载更多完成
                    if (!isLast) {
                        mTextViewMore.setText("加载完成");
                    }
                    mProgressBarMore.setVisibility(View.GONE);
                    myHandler.sendEmptyMessageDelayed(MSG_3, DELAY_GONE);
                    break;
                }
                case MSG_3: {
                    //  加载完成，隐藏footer，并归零
                    mProgressBarMore.setVisibility(View.GONE);
                    mTextViewMore.setVisibility(View.GONE);

                    myHandler.sendEmptyMessageDelayed(Data.MSG_4, 200);
                    break;
                }
                case Data.MSG_4: {
                    //  保存不刷新两次
                    addAble = true;
                    break;
                }
                case Data.MSG_5: {
                    showPopupWindow();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: ");
                }
            }
        }
    }
}
