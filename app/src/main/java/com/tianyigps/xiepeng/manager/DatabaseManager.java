package com.tianyigps.xiepeng.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    //  增，维修
    public void addRepair(String tNo, String position, String positionPic, String installPic, String explain) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNo", tNo);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(Data.DATA_TAB_REPAIR, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase insert error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  删，维修
    public void deleteRepair(String tNo) {
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(Data.DATA_TAB_REPAIR
                    , "tNo=?"
                    , new String[]{tNo});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修
    public void modifyRepair(String tNo, String position, String positionPic, String installPic, String explain) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNo", tNo);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "tNo=?"
                    , new String[]{tNo});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  查，维修
    public Cursor getRepair() {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"tNo, position, positionPic, installPic, explain"}
                    , null
                    , null
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();

            return cursor;
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return null;
    }

    //  查，维修，重载
    public Cursor getRepair(String tNo) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"tNo, position, positionPic, installPic, explain"}
                    , "tNo=?"
                    , new String[]{tNo}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();

            return cursor;
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return null;
    }

    //=============================================华丽的分割线===================================================

    public void close() {
        mSqLiteDatabase.close();
    }
}
