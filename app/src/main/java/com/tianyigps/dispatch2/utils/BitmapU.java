package com.tianyigps.dispatch2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by cookiemouse on 2017/8/17.
 */

public class BitmapU {

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param resId id
     * @return Bitmap
     */
    public static Bitmap getBitmap(Context context, int resId) {
        InputStream is = context.getResources().openRawResource(resId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param resId id
     * @return Bitmap
     */
    public static Bitmap getBitmap(Context context, int resId, int width, int height) {
        InputStream is = context.getResources().openRawResource(resId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.outWidth = width;
        options.outHeight = height;
        return BitmapFactory.decodeStream(is, null, options);
    }
}
