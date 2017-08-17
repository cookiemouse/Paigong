package com.tianyigps.dispatch2.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.tianyigps.dispatch2.manager.FileManager;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.BitmapCallback;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;

/**
 * Created by cookiemouse on 2017/7/28.
 */

public class TinyU {

    private static final String TAG = "TinyU";
    private static String tempPath;
    private static Bitmap bitmapR;

    public static String tinyPic(String path) {
        FileManager fileManager = new FileManager();
        tempPath = fileManager.getPath();
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(path).asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                Log.i(TAG, "callback: outfile-->" + outfile);

                File srcFile = new File(outfile);
                if (!srcFile.exists() || !srcFile.isFile()) {
                    Log.i(TAG, "callback: 文件不存在");
                    return;
                }
                Log.i(TAG, "callback: -->" + srcFile.renameTo(new File(tempPath)));
            }
        });
        return tempPath;
    }

    public static void tinyPic(String path, FileCallback fileCallback) {
        FileManager fileManager = new FileManager();
        tempPath = fileManager.getPath();
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(path).asFile().withOptions(options).compress(fileCallback);
    }

    public static void tinyPic(Bitmap bitmap, BitmapCallback bitmapCallback) {
        Tiny.BitmapCompressOptions options = new Tiny.BatchFileCompressOptions();
        Tiny.getInstance().source(bitmap).asBitmap().withOptions(options).compress(bitmapCallback);
    }
}
