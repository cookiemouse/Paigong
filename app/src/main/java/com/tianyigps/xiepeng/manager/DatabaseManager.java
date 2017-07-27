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
    public void addRepair(String tNo, String position, String positionPic, String installPic, String explain, String newtNo) {
        if (repairExist(tNo)) {
            modifyRepair(tNo, position, positionPic, installPic, explain, newtNo);
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNo", tNo);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);
        contentValues.put("newtNo", newtNo);

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

    //  增，维修
    public void addRepair(String tNo, String position, String explain) {
        addRepairPosition(tNo, position);
        addRepairExplain(tNo, explain);
    }

    //  增，维修
    public void addRepair(String tNo, String position, String explain, String newtNo) {
        addRepairPosition(tNo, position);
        addRepairExplain(tNo, explain);
        addRepairNewtNo(tNo, newtNo);
    }

    //  增，维修，重载
    public void addRepairPosition(String tNo, String position) {
        if (repairExist(tNo)) {
            modifyRepairPosition(tNo, position);
            return;
        }
        addRepair(tNo, position, null, null, null, null);
    }

    //  增，维修，重载
    public void addRepairPositionPic(String tNo, String positionPic) {
        if (repairExist(tNo)) {
            modifyRepairPositionPic(tNo, positionPic);
            return;
        }
        addRepair(tNo, null, positionPic, null, null, null);
    }

    //  增，维修，重载
    public void addRepairInstallPic(String tNo, String installPic) {
        if (repairExist(tNo)) {
            modifyRepairInstallPic(tNo, installPic);
            return;
        }
        addRepair(tNo, null, null, installPic, null, null);
    }

    //  增，维修，重载
    public void addRepairExplain(String tNo, String explain) {
        if (repairExist(tNo)) {
            modifyRepairExplain(tNo, explain);
            return;
        }
        addRepair(tNo, null, null, null, explain, null);
    }

    //  增，维修，重载
    public void addRepairNewtNo(String tNo, String newtNo) {
        if (repairExist(tNo)) {
            modifyRepairExplain(newtNo, newtNo);
            return;
        }
        addRepair(tNo, null, null, null, null, newtNo);
    }

    //  删，维修
    public void deleteRepair(String tNo) {
        if (!repairExist(tNo)) {
            return;
        }
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
    public void modifyRepair(String tNo, String position, String positionPic, String installPic, String explain, String newtNo) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);
        contentValues.put("newtNo", newtNo);

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

    //  改，维修，重载
    public void modifyRepairPosition(String tNo, String position) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("position", position);

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

    //  改，维修，重载
    public void modifyRepairPositionPic(String tNo, String positionPic) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("positionPic", positionPic);

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

    //  改，维修，重载
    public void modifyRepairInstallPic(String tNo, String installPic) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("installPic", installPic);

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

    //  改，维修，重载
    public void modifyRepairExplain(String tNo, String explain) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
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
    //  改，维修，重载
    public void modifyRepairnewtNo(String tNo, String newtNo) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("newtNo", newtNo);

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
    public Cursor getRepairs() {
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

    //  查，是否存在该order
    public boolean repairExist(String tNo) {
        Cursor cursor = getRepair(tNo);
        return cursor.moveToFirst();
    }

    //=============================================华丽的分割线===================================================

    public void close() {
        mSqLiteDatabase.close();
    }
}
