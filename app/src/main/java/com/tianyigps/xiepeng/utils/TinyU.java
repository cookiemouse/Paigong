package com.tianyigps.xiepeng.utils;

import android.util.Log;

import com.tianyigps.xiepeng.manager.FileManager;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;

/**
 * Created by cookiemouse on 2017/7/28.
 */

public class TinyU {

    private static final String TAG = "TinyU";
    private static String tempPath;

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
}
