package com.tianyigps.xiepeng.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tianyigps.xiepeng.data.Data;

/**
 * Created by cookiemouse on 2017/7/26.
 */

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private SQLiteDatabase mSqLiteDatabase;

    public DatabaseManager(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context, Data.DATA_DB_NAME);
        mSqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public void close() {
        mSqLiteDatabase.close();
    }
}
