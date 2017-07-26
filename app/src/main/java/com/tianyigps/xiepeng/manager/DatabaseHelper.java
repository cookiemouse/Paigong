package com.tianyigps.xiepeng.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tianyigps.xiepeng.data.Data;

/**
 * Created by cookiemouse on 2017/7/26.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int VERSION = 1;
    private static final String DATA_TABLE = "create table " + Data.DATA_TAB_NAME + "()";

    public DatabaseHelper(Context context, String name) {
        this(context, name, null, VERSION);
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate: -->" + 1);

        Log.i(TAG, "onCreate: -->" + 2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
