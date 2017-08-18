package com.tianyigps.dispatch2.utils;

import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by cookiemouse on 2017/8/18.
 */

public class SnackbarU {

    private Snackbar mSnackbar;

    public SnackbarU make(View view, String msg, int duration) {
        mSnackbar = Snackbar.make(view, msg, duration);
        return this;
    }

    public SnackbarU make(View view, View childView, int duration) {
        this.make(view, "", duration);
        this.setView(childView);
        return this;
    }


    public SnackbarU setBackground(Drawable drawable) {
        if (null == mSnackbar) {
            throw new NullPointerException();
        }
        View view = mSnackbar.getView();
        view.setBackgroundDrawable(drawable);
        return this;
    }

    public SnackbarU setBackground(int color) {
        if (null == mSnackbar) {
            throw new NullPointerException();
        }
        View view = mSnackbar.getView();
        view.setBackgroundColor(color);
        return this;
    }

    public SnackbarU setView(View childView) {
        if (null == mSnackbar) {
            throw new NullPointerException();
        }
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) mSnackbar.getView();
        layout.addView(childView);
        return this;
    }

    public SnackbarU setGravity(int gravity) {
        if (null == mSnackbar) {
            throw new NullPointerException();
        }
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.gravity = gravity;
        mSnackbar.getView().setLayoutParams(layoutParams1);
        return this;
    }

    public void show() {
        if (null == mSnackbar) {
            throw new NullPointerException();
        }
        mSnackbar.show();
    }
}
