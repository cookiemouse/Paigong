package com.tianyigps.xiepeng.manager;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cookiemouse on 2017/7/26.
 */

public class FileManager {

    private static final String TAG = "FileManager";

    private File mFile;

    public FileManager(String path) {
        mFile = new File(path);
        if (!mFile.exists()) {
            if (mFile.mkdirs()) {
                Log.i(TAG, "FileManager: make dirs");
            }
        }
    }

    public boolean isExists() {
        return mFile.exists();
    }

    public void delete() {
        if (isExists()) {
            mFile.delete();
        }
    }

    public void saveBitmapPng(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBitmapJpg(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return mFile.getPath();
    }
}
