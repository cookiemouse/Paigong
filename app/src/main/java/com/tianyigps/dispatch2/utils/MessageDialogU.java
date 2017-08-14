package com.tianyigps.dispatch2.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tianyigps.dispatch2.R;

/**
 * Created by cookiemouse on 2017/7/27.
 */

public class MessageDialogU {
    private Context context;

    public MessageDialogU(Context context) {
        this.context = context;
    }

    public void show(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    public void showAndFinish(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
