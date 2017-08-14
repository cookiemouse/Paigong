package com.tianyigps.dispatch2.manager;

import android.graphics.Bitmap;
import android.os.Environment;
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

    public static final int TYPE_PNG = 1;
    public static final int TYPE_JPEG = 2;

    private static final String DIRECT = Environment.getExternalStorageDirectory().getPath() + "/paigong/";

    private File mFile;

    public FileManager(String name) {
        File file = new File(DIRECT);
        if (file.mkdirs()) {
            Log.i(TAG, "FileManager: make dirs");
        }
//        if (!mFile.exists()) {
//            delete();
//        }
        String path = DIRECT + name;
        mFile = new File(path);
    }

    public FileManager() {
        this("temp.png");
    }

    public FileManager(int type) {
        String path;
        switch (type) {
            case TYPE_PNG: {
                path = DIRECT + System.currentTimeMillis() + ".png";
                break;
            }
            case TYPE_JPEG: {
                path = DIRECT + System.currentTimeMillis() + ".jpg";
                break;
            }
            default: {
                path = DIRECT + System.currentTimeMillis() + ".png";
            }
        }
        mFile = new File(path);
    }


    public boolean isExists() {
        return mFile.exists();
    }

    public boolean delete() {
        if (isExists()) {
            return mFile.delete();
        }
        return false;
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

    public File file() {
        return mFile;
    }
}
