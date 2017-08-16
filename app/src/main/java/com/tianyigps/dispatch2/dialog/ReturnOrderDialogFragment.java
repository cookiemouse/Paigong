package com.tianyigps.dispatch2.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;
import com.tianyigps.dispatch2.interfaces.OnInstallBackListener;
import com.tianyigps.dispatch2.manager.NetworkManager;
import com.tianyigps.dispatch2.manager.SharedpreferenceManager;

import static com.tianyigps.dispatch2.data.Data.DATA_INTENT_ORDER_NO;
import static com.tianyigps.dispatch2.data.Data.MSG_1;
import static com.tianyigps.dispatch2.data.Data.MSG_ERO;

/**
 * Created by djc on 2017/7/19.
 */

public class ReturnOrderDialogFragment extends DialogFragment {

    private static final String TAG = "ReturnOrderDialog";

    private NetworkManager mNetworkManager;
    private MyHandler myHandler;
    private SharedpreferenceManager mSharedpreferenceManager;

    private int eid;
    private String userName;
    private String token, orderNo, chooseReason, filledReason;

    private OnFinishListener mOnFinishListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  获取参数
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        //  返回null，让该Fragment来管理Dialog

        mNetworkManager = new NetworkManager();
        mSharedpreferenceManager = new SharedpreferenceManager(getActivity());
        myHandler = new MyHandler();

        eid = mSharedpreferenceManager.getEid();
        token = mSharedpreferenceManager.getToken();
        userName = mSharedpreferenceManager.getAccount();
        orderNo = getArguments().getString(DATA_INTENT_ORDER_NO);

        mNetworkManager.setInstallBackListener(new OnInstallBackListener() {
            @Override
            public void onFailure() {
                myHandler.sendEmptyMessage(MSG_ERO);
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onSuccess(String result) {
                myHandler.sendEmptyMessage(MSG_1);
            }
        });

        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_return_order, null);

        final RadioGroup radioGroup = viewDialog.findViewById(R.id.rg_layout_return_order);
        final EditText editTextReason = viewDialog.findViewById(R.id.et_layout_return_order);
        final TextView textViewCount = viewDialog.findViewById(R.id.tv_layout_return_order_count);
        TextView textViewCancel = viewDialog.findViewById(R.id.tv_layout_return_order_cancel);
        TextView textViewSubmit = viewDialog.findViewById(R.id.tv_layout_return_order_submit);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rb_layout_return_order_other) {
                    editTextReason.setVisibility(View.VISIBLE);
                    textViewCount.setVisibility(View.VISIBLE);
                } else {
                    editTextReason.setVisibility(View.GONE);
                    textViewCount.setVisibility(View.GONE);

                }
            }
        });

        editTextReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "onTextChanged: " + i);
                String count = (i + 1) + "/200";
                textViewCount.setText(count);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/19 提交
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_layout_return_order_timeout: {
                        chooseReason = "0";
                        break;
                    }
                    case R.id.rb_layout_return_order_no_device: {
                        chooseReason = "1";
                        break;
                    }
                    case R.id.rb_layout_return_order_manager_ask: {
                        chooseReason = "2";
                        break;
                    }
                    case R.id.rb_layout_return_order_other: {
                        chooseReason = "3";
                        break;
                    }
                    default: {
                        chooseReason = "";
                    }
                }

                filledReason = editTextReason.getText().toString();

                mNetworkManager.installBack(eid, token, orderNo, chooseReason, filledReason, userName);
            }
        });

        builder.setView(viewDialog);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_message_order_cancel, null);
        builder.setView(viewDialog);
        TextView textViewCancel = viewDialog.findViewById(R.id.tv_dialog_message_order_cancel);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/7/19 知道了
                //  dismiss();
                if (null == mOnFinishListener){
                    throw new NullPointerException("OnFinishListener is null");
                }
                mOnFinishListener.onFinish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERO: {
                    break;
                }
                case MSG_1: {
                    dismiss();
                    Log.i(TAG, "handleMessage: 订单已被取消");
                    showSuccessDialog();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default");
                }
            }
        }
    }

    public interface OnFinishListener{
        void onFinish();
    }

    public void setOnFinishListener(OnFinishListener listener){
        this.mOnFinishListener = listener;
    }
}
