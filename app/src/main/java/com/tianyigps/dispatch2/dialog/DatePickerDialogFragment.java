package com.tianyigps.dispatch2.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.tianyigps.dispatch2.R;

/**
 * Created by djc on 2017/7/14.
 */

public class DatePickerDialogFragment extends DialogFragment {

    private static final String TAG = "DialogFragment";

    private NumberPicker mNumberPickerYear, mNumberPickerMonth;
    private TextView mTextViewCancel, mTextViewEnsure;

    private OnEnsureListener mOnEnsureListener;

    private int mIntYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewDialog = inflater.inflate(R.layout.dialog_date_picker, container, false);

        init(viewDialog);

        setEventListener();

        return viewDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //  2017/7/14 what showed be happen
        return super.onCreateDialog(savedInstanceState);
    }

    private void init(View view) {
        mNumberPickerYear = view.findViewById(R.id.np_dialog_picker_year);
        mNumberPickerMonth = view.findViewById(R.id.np_dialog_picker_month);

        mTextViewCancel = view.findViewById(R.id.tv_dialog_picker_cancel);
        mTextViewEnsure = view.findViewById(R.id.tv_dialog_picker_ensure);

        mNumberPickerMonth.setDisplayedValues(getResources().getStringArray(R.array.picker_month));
        mNumberPickerMonth.setMinValue(0);
        mNumberPickerMonth.setMaxValue(11);
        mNumberPickerMonth.setWrapSelectorWheel(false);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
//        Date date = new Date(System.currentTimeMillis());
//        String year = simpleDateFormat.format(date);
//        mIntYear = Integer.valueOf(year);
        mIntYear = 2017;
        String[] strings = new String[14];
        for (int i = 0; i < 14; i++) {
            strings[i] = "" + (mIntYear + i);
        }
        mNumberPickerYear.setDisplayedValues(strings);
        mNumberPickerYear.setMinValue(0);
        mNumberPickerYear.setMaxValue(strings.length - 1);
        mNumberPickerYear.setWrapSelectorWheel(false);
    }

    private void setEventListener() {

        mTextViewEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/14 选择时间
                Log.i(TAG, "onSignClick: year-->" + (mIntYear + mNumberPickerYear.getValue()));
                Log.i(TAG, "onSignClick: month-->" + (mNumberPickerMonth.getValue() + 1));
                if (null == mOnEnsureListener) {
                    throw new NullPointerException("OnEnsureListener is null, pleases setOnEnsureListener");
                }
                mOnEnsureListener.onEnsure(mIntYear + mNumberPickerYear.getValue()
                        , mNumberPickerMonth.getValue() + 1);

                dismiss();
            }
        });

        mTextViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  2017/7/14 取消
                dismiss();
            }
        });
    }

    //  确定时返回的年、月数据接口
    public interface OnEnsureListener {
        void onEnsure(int year, int month);
    }

    public void setOnEnsureListener(OnEnsureListener listener) {
        this.mOnEnsureListener = listener;
    }
}
