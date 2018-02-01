package com.tianyigps.dispatch2.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tianyigps.dispatch2.data.Data;

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

    //  增，订单
    public void addOrder(String orderNo, int carId, int tid) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_ORDER
                    , new String[]{"orderNo, carId, tid"}
                    , "tid=?"
                    , new String[]{"" + tid}
                    , null, null, null);

            if (null != cursor && cursor.moveToFirst()) {
                return;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("orderNo", orderNo);
                contentValues.put("carId", carId);
                contentValues.put("tid", tid);

                mSqLiteDatabase.insert(Data.DATA_TAB_ORDER, null, contentValues);
            }

            mSqLiteDatabase.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  删，订单
    public void deleteOrder(String orderNo) {
        if (!orderExist(orderNo)) {
            return;
        }
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(Data.DATA_TAB_ORDER
                    , "orderNo=?"
                    , new String[]{orderNo});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，订单
    public void modifyOrder(String orderNo, int carId, int tid) {
        if (!orderExist(orderNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carId", carId);
        contentValues.put("tid", tid);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_ORDER
                    , contentValues
                    , "orderNo=?"
                    , new String[]{orderNo});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  查，是否存在该订单
    public boolean orderExist(String orderNo) {
        Cursor cursor = getOrder(orderNo);
        return null != cursor && cursor.moveToFirst();
    }

    //  查，订单
    public Cursor getOrder(String orderNo) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_ORDER
                    , new String[]{"orderNo, carId, tid"}
                    , "orderNo=?"
                    , new String[]{orderNo}
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

    //=====================订单========================华丽的分割线=======================维修============================

    //  增，维修
    private void addRepair(int idMain) {
        if (repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("idMain", idMain);

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
    public void addRepair(int idMain, String tNo) {
        if (repairExist(idMain)) {
            modifyRepair(idMain, tNo);
            return;
        }
        this.addRepair(idMain);
        this.addRepair(idMain, tNo);
    }

    //  增，维修
    public void addRepair(int idMain, String tNo, String position, String positionPic, String installPic, String explain
            , String newImei, String positionUrl, String installUrl, int model, int wire) {
        if (repairExist(idMain)) {
            modifyRepair(idMain, tNo, position, positionPic, installPic, explain, newImei, positionUrl, installUrl, model, wire);
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("idMain", idMain);
        contentValues.put("tNo", tNo);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);
        contentValues.put("newImei", newImei);
        contentValues.put("positionUrl", positionUrl);
        contentValues.put("installUrl", installUrl);
        contentValues.put("model", model);
        contentValues.put("wire", wire);

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
    public void addRepair(int idMain, int model) {
        if (repairExist(idMain)) {
            modifyRepairModel(idMain, model);
            return;
        }
        this.addRepair(idMain);
        this.addRepair(idMain, model);
    }

    //  增,维修
    public void addRepairReplace(int idMain, int replace) {
        if (repairExist(idMain)) {
            modifyRepairReplace(idMain, replace);
            return;
        }
        this.addRepair(idMain);
        this.addRepairReplace(idMain, replace);
    }

    // 增，维修
    public void addRepairWire(int idMain, int wire) {
        if (repairExist(idMain)) {
            modifyRepairWire(idMain, wire);
            return;
        }
        this.addRepair(idMain);
        this.addRepairWire(idMain, wire);
    }

    //  增，维修
    public void addRepair(int idMain, String position, String explain) {
        addRepairPosition(idMain, position);
        addRepairExplain(idMain, explain);
    }

    //  增，维修
    public void addRepair(int idMain, String position, String explain, String newImei) {
        addRepairPosition(idMain, position);
        addRepairExplain(idMain, explain);
        addRepairNewImei(idMain, newImei);
    }

    //  增,T，locateType
    public void addRepairLocateType(int idMain, int locateType) {
        if (repairExist(idMain)) {
            modifyRepairLocateType(idMain, locateType);
            return;
        }
        this.addRepair(idMain);
        this.addRepairLocateType(idMain, locateType);
    }

    public void modifyRepairLocateType(int idMain, int locateType) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("locateType", locateType);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  增，维修，重载
    public void addRepairPosition(int idMain, String position) {
        if (repairExist(idMain)) {
            modifyRepairPosition(idMain, position);
            return;
        }
        this.addRepair(idMain);
        this.addRepairPosition(idMain, position);
    }

    //  增，维修，重载
    public void addRepairPositionPic(int idMain, String positionPic, String positionUrl) {
        if (repairExist(idMain)) {
            modifyRepairPositionPic(idMain, positionPic, positionUrl);
            return;
        }
        this.addRepair(idMain);
        this.addRepairPositionPic(idMain, positionPic, positionUrl);
    }

    //  增，维修，重载
    public void addRepairInstallPic(int idMain, String installPic, String installUrl) {
        if (repairExist(idMain)) {
            modifyRepairInstallPic(idMain, installPic, installUrl);
            return;
        }
        this.addRepair(idMain);
        this.addRepairInstallPic(idMain, installPic, installUrl);
    }

    //  增，维修，重载
    public void addRepairExplain(int idMain, String explain) {
        if (repairExist(idMain)) {
            modifyRepairExplain(idMain, explain);
            return;
        }
        this.addRepair(idMain);
        this.addRepairExplain(idMain, explain);
    }

    //  增，维修，重载
    public void addRepairNewImei(int idMain, String newImei) {
        if (repairExist(idMain)) {
            modifyRepairNewImei(idMain, newImei);
            return;
        }
        this.addRepair(idMain);
        this.addRepairNewImei(idMain, newImei);
    }

    //  增，维修，重载
    public void addRepairPositionUrl(int idMain, String positionUrl) {
        if (repairExist(idMain)) {
            modifyRepairPositionUrl(idMain, positionUrl);
            return;
        }
        this.addRepair(idMain);
        this.addRepairPositionUrl(idMain, positionUrl);
    }

    //  增，维修，重载
    public void addRepairInstallUrl(int idMain, String installUrl) {
        if (repairExist(idMain)) {
            modifyRepairInstallUrl(idMain, installUrl);
            return;
        }
        this.addRepair(idMain);
        this.addRepairInstallUrl(idMain, installUrl);
    }

    //  删，维修
    public void deleteRepair(int idMain) {
        if (!repairExist(idMain)) {
            return;
        }
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(Data.DATA_TAB_REPAIR
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修
    public void modifyRepair(int idMain, String tNo) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNo", tNo);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修
    public void modifyRepair(int idMain, String tNo, String position, String positionPic, String installPic, String explain,
                             String newImei, String positionUrl, String installUrl, int model, int wire) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNo", tNo);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);
        contentValues.put("newImei", newImei);
        contentValues.put("positionUrl", positionUrl);
        contentValues.put("installUrl", installUrl);
        contentValues.put("model", model);
        contentValues.put("wire", wire);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairModel(int idMain, int model) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("model", model);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairReplace(int idMain, int replace) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("replace", replace);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairWire(int idMain, int wire) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("wire", wire);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairPosition(int idMain, String position) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("position", position);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairPositionPic(int idMain, String positionPic, String positionUrl) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("positionPic", positionPic);
        contentValues.put("positionUrl", positionUrl);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairInstallPic(int idMain, String installPic, String installUrl) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("installPic", installPic);
        contentValues.put("installUrl", installUrl);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairExplain(int idMain, String explain) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("explain", explain);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairNewImei(int idMain, String newImei) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("newImei", newImei);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairPositionUrl(int idMain, String positionUrl) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("positionUrl", positionUrl);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，维修，重载
    public void modifyRepairInstallUrl(int idMain, String installUrl) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("installUrl", installUrl);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_REPAIR
                    , contentValues
                    , "idMain=?"
                    , new String[]{"" + idMain});
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
                    , new String[]{"idMain, tNo, position, positionPic, installPic, explain, newImei" +
                            ", positionUrl, installUrl, model"}
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
    public Cursor getRepair(int idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"idMain, tNo, position, positionPic, installPic, explain, newImei" +
                            ", positionUrl, installUrl, model, locateType, wire, replace"}
                    , "idMain=?"
                    , new String[]{"" + idMain}
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
    public String getRepairPositionPic(int idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"positionPic"}
                    , "idMain=?"
                    , new String[]{"" + idMain}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();

            if (cursor.moveToFirst()) {
                String path = cursor.getString(0);
                cursor.close();
                return path;
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return null;
    }

    //  查，维修，重载
    public String getRepairInstallPic(int idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"installPic"}
                    , "idMain=?"
                    , new String[]{"" + idMain}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();

            if (cursor.moveToFirst()) {
                String path = cursor.getString(0);
                cursor.close();
                return path;
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return null;
    }

    //  查，维修，重载
    public String getRepairPositionUrl(int idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"positionUrl"}
                    , "idMain=?"
                    , new String[]{"' + idMain"}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();

            if (cursor.moveToFirst()) {
                String path = cursor.getString(0);
                cursor.close();
                return path;
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return null;
    }

    //  查，维修，重载
    public String getRepairInstallUrl(int idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"installUrl"}
                    , "idMain=?"
                    , new String[]{"" + idMain}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();

            if (cursor.moveToFirst()) {
                String path = cursor.getString(0);
                cursor.close();
                return path;
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return null;
    }

    //  查，是否存在该order
    public boolean repairExist(int idMain) {
        Cursor cursor = getRepair(idMain);
        return null != cursor && cursor.moveToFirst();
    }

    //  查，是否存在该order
    public boolean repairExistByImei(String imei) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{}
                    , "newImei=?"
                    , new String[]{imei}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();

            return (null != cursor && cursor.moveToFirst());
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return false;
    }

    //=====================维修========================华丽的分割线=======================车辆============================

    //=====================车辆========================华丽的分割线=======================设备============================

//    //  增，T
//    public void addTer(String idMain) {
//        if (terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("idMain", idMain);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.insert(Data.DATA_TAB_INSTALL_TERMINAL, null, contentValues);
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqliteDatabase insert error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  增，T
//    public void addTer(String idMain, String tNoOld, String tNoNew, String position, String positionPic
//            , String installPic, String positionPicUri, String installPicUri, int carId, int wire) {
//        if (terExist(idMain)) {
//            modifyTer(idMain, tNoOld, tNoNew, position, positionPic, installPic, positionPicUri, installPicUri, carId, wire);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTer(idMain, tNoOld, tNoNew, position, positionPic, installPic, positionPicUri, installPicUri, carId, wire);
//    }
//
//    //  增，T
//    public void addTer(String idMain, int carId) {
//        if (terExist(idMain)) {
//            modifyTer(idMain, carId);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTer(idMain, carId);
//    }
//
//    //  增, T, info
//    public void addTerInfo(String idMain, String tNoNew, String position) {
//        if (terExist(idMain)) {
//            modifyTer(idMain, tNoNew, position);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTerInfo(idMain, tNoNew, position);
//    }
//
//    //  增, T, info
//    public void addTerInfo(String idMain, String tNoOld, String tNoNew, String position) {
//        if (terExist(idMain)) {
//            modifyTer(idMain, tNoOld, tNoNew, position);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTerInfo(idMain, tNoOld, tNoNew, position);
//    }
//
//    //  增，T, tId
//    public void addTerId(String idMain, int tId, int carId) {
//        if (terExist(idMain)) {
//            modifyTerId(idMain, tId, carId);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTerId(idMain, tId, carId);
//    }
//
//    //  增,T，model
//    public void addTerModel(String idMain, int model) {
//        if (terExist(idMain)) {
//            modifyTerModel(idMain, model);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTerModel(idMain, model);
//    }
//
//    //  增,T，locateType
//    public void addTerLocateType(String idMain, int locateType) {
//        if (terExist(idMain)) {
//            modifyTerLocateType(idMain, locateType);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTerLocateType(idMain, locateType);
//    }
//
//    //  增，T，PositionPic
//    public void addTerPositionPic(String idMain, String positionPic, String positionPicUri) {
//        if (terExist(idMain)) {
//            modifyTerPositionPic(idMain, positionPic, positionPicUri);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTerPositionPic(idMain, positionPic, positionPicUri);
//    }
//
//    //  增，T，InstallPic
//    public void addTerInstallPic(String idMain, String installPic, String installPicUri) {
//        if (terExist(idMain)) {
//            modifyTerInstallPic(idMain, installPic, installPicUri);
//            return;
//        }
//        this.addTer(idMain);
//        this.addTerInstallPic(idMain, installPic, installPicUri);
//    }
//
//    //  删，T
//    public void deleteTer(String idMain) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.delete(Data.DATA_TAB_INSTALL_TERMINAL
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqliteDatabase delete error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  删，T
//    public void deleteTerByTid(int tId) {
//        if (!terExistByTid(tId)) {
//            return;
//        }
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.delete(Data.DATA_TAB_INSTALL_TERMINAL
//                    , "tId=?"
//                    , new String[]{"" + tId});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqliteDatabase delete error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T
//    public void modifyTer(String idMain, int carId) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("carId", carId);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T
//    public void modifyTer(String idMain, String tNoOld, String tNoNew, String position
//            , String positionPic, String installPic, String positionPicUri, String installPicUri, int carId, int wire) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("tNoOld", tNoOld);
//        contentValues.put("tNoNew", tNoNew);
//        contentValues.put("position", position);
//        contentValues.put("positionPic", positionPic);
//        contentValues.put("installPic", installPic);
//        contentValues.put("positionPicUri", positionPicUri);
//        contentValues.put("installPicUri", installPicUri);
//        contentValues.put("carId", carId);
//        contentValues.put("wire", wire);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T，重载
//    public void modifyTer(String idMain, String tNoNew, String position) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("tNoNew", tNoNew);
//        contentValues.put("position", position);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T, 重载
//    public void modifyTerId(String idMain, int tId, int carId) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        if (this.getTerId(idMain) == tId) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("tId", tId);
//        contentValues.put("carId", carId);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T, 重载
//    public void modifyTerLocateType(String idMain, int locateType) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("locateType", locateType);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T，重载
//    public void modifyTerModel(String idMain, int model) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("model", model);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T，重载
//    public void modifyTer(String idMain, String tNoOld, String tNoNew, String position) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("tNoOld", tNoOld);
//        contentValues.put("tNoNew", tNoNew);
//        contentValues.put("position", position);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T，重载
//    public void modifyTerPositionPic(String idMain, String positionPic, String positionPicUri) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("positionPic", positionPic);
//        contentValues.put("positionPicUri", positionPicUri);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  改，T，重载
//    public void modifyTerInstallPic(String idMain, String installPic, String installPicUri) {
//        if (!terExist(idMain)) {
//            return;
//        }
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("installPic", installPic);
//        contentValues.put("installPicUri", installPicUri);
//
//        mSqLiteDatabase.beginTransaction();
//        try {
//            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
//                    , contentValues
//                    , "idMain=?"
//                    , new String[]{(idMain)});
//            mSqLiteDatabase.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqLiteDatabase update error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//    }
//
//    //  查，T
//    public Cursor getTer(String idMain) {
//        mSqLiteDatabase.beginTransaction();
//        try {
//            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_TERMINAL
//                    , new String[]{"idMain, tNoOld, tNoNew, position"
//                            + ", positionPic, installPic"
//                            + ", positionPicUri, installPicUri"
//                            + ", model, tId, locateType, carId, wire"}
//                    , "idMain=?"
//                    , new String[]{(idMain)}
//                    , null, null, null);
//            mSqLiteDatabase.setTransactionSuccessful();
//
//            return cursor;
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqliteDatabase query error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//        return null;
//    }
//
//    //  查，T
//    public Cursor getTer(int tId) {
//        mSqLiteDatabase.beginTransaction();
//        try {
//            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_TERMINAL
//                    , new String[]{"idMain, tNoOld, tNoNew, position"
//                            + ", positionPic, installPic"
//                            + ", positionPicUri, installPicUri"
//                            + ", model, tId, locateType, carId, wire"}
//                    , "tId=?"
//                    , new String[]{("" + tId)}
//                    , null, null, null);
//            mSqLiteDatabase.setTransactionSuccessful();
//
//            return cursor;
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqliteDatabase query error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//        return null;
//    }
//
//    //  查，T
//    public Cursor getTerByCarId(int carId) {
//        mSqLiteDatabase.beginTransaction();
//        try {
//            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_TERMINAL
//                    , new String[]{"idMain, tNoOld, tNoNew, position"
//                            + ", positionPic, installPic"
//                            + ", positionPicUri, installPicUri"
//                            + ", model, tId, locateType, carId, wire"}
//                    , "carId=?"
//                    , new String[]{("" + carId)}
//                    , null, null, null);
//            mSqLiteDatabase.setTransactionSuccessful();
//
//            return cursor;
//        } catch (Exception e) {
//            Log.e(TAG, e + "SqliteDatabase query error");
//        } finally {
//            mSqLiteDatabase.endTransaction();
//        }
//        return null;
//    }
//
//    public int getTerId(String idMain) {
//        int id = 0;
//        Cursor cursor = this.getTer(idMain);
//        if (null != cursor && cursor.moveToFirst()) {
//            id = cursor.getInt(9);
//        }
//        return id;
//    }
//
//    //  查，T，重载
//    public boolean terExist(String idMain) {
//        Cursor cursor = getTer(idMain);
//        return null != cursor && cursor.moveToFirst();
//    }
//
//    //  查，T，重载
//    public boolean terExistByTid(int tId) {
//        Cursor cursor = getTer(tId);
//        return null != cursor && cursor.moveToFirst();
//    }

    //  ====================================车辆====2======================================
    public void addCar2(int carId) {
        if (carExist2(carId)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carId", carId);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(Data.DATA_TAB_INSTALL_CAR_2, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase insert error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void addCar2Info(int carId, String carNo, String frameNo, String carType) {
        if (carExist2(carId)) {
            modifyCar2(carId, carNo, frameNo, carType);
            return;
        }
        this.addCar2(carId);
        this.addCar2Info(carId, carNo, frameNo, carType);
    }

    public void addCar2Info(int carId, String carNo, String frameNo, String carType, String carNoPic, String frameNoPic) {
        if (carExist2(carId)) {
            modifyCar2(carId, carNo, frameNo, carType, carNoPic, frameNoPic);
            return;
        }
        this.addCar2(carId);
        this.addCar2Info(carId, carNo, frameNo, carType, carNoPic, frameNoPic);
    }

    public void deleteCar2(int carId) {
        if (!carExist2(carId)) {
            return;
        }
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(Data.DATA_TAB_INSTALL_CAR_2
                    , "carId=?"
                    , new String[]{("" + carId)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyCar2CarNoPic(int carId, String carNoPic) {
        if (!carExist2(carId)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carNoPic", carNoPic);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR_2
                    , contentValues
                    , "carId=?"
                    , new String[]{("" + carId)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyCar2FrameNoPic(int carId, String frameNoPic) {
        if (!carExist2(carId)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("frameNoPic", frameNoPic);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR_2
                    , contentValues
                    , "carId=?"
                    , new String[]{("" + carId)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyCar2(int carId, String carNo, String frameNo, String carType) {
        if (!carExist2(carId)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carNo", carNo);
        contentValues.put("frameNo", frameNo);
        contentValues.put("carType", carType);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR_2
                    , contentValues
                    , "carId=?"
                    , new String[]{("" + carId)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyCar2(int carId, String carNo, String frameNo, String carType, String carNoPic, String frameNoPic) {
        if (!carExist2(carId)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carNo", carNo);
        contentValues.put("frameNo", frameNo);
        contentValues.put("carType", carType);
        contentValues.put("carNoPic", carNoPic);
        contentValues.put("frameNoPic", frameNoPic);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR_2
                    , contentValues
                    , "carId=?"
                    , new String[]{("" + carId)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public boolean carExist2(int carId) {
        Cursor cursor = getCar2(carId);
        return null != cursor && cursor.moveToFirst();
    }

    public Cursor getCar2(int carId) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_CAR_2
                    , new String[]{"carId, carNo, frameNo, carType, carNoPic, frameNoPic"}
                    , "carId=?"
                    , new String[]{("" + carId)}
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

    //  ====================================设备====2======================================

    public void addTer2(int carId, int item) {
        if (terExist2(carId, item)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carId", carId);
        contentValues.put("item", item);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(Data.DATA_TAB_INSTALL_TERMINAL_2, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase insert error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void addTer2(int carId, int item, String tNoOld, String tNoNew, String position, String positionPic, String installPic, int wire) {
        if (terExist2(carId, item)) {
            modifyTer2(carId, item, tNoOld, tNoNew, position, positionPic, installPic, wire);
            return;
        }
        this.addTer2(carId, item);
        this.addTer2(carId, item, tNoOld, tNoNew, position, positionPic, installPic, wire);
    }

    public void addTer2LocateType(int carId, int item, int locateType){
        if (terExist2(carId, item)) {
            modifyTer2LocateType(carId, item, locateType);
            return;
        }
        this.addTer2(carId, item);
        this.addTer2LocateType(carId, item, locateType);
    }

    public void addTer2ByTid(int tid, String tNoOld, String tNoNew, String position, String positionPic, String installPic, int carId
            , int locateType, int wire){
        if (terExist2ByTid(tid)) {
            modifyTer2ByTid(tid, tNoOld, tNoNew, position, positionPic, installPic, carId, locateType, wire);
            return;
        }
        this.addTer2ByTid(tid, tNoOld, tNoNew, position, positionPic, installPic, carId, locateType, wire);
    }

    //  改，T, 重载
    public void modifyTer2LocateType(int carId, int item, int locateType) {
        if (!terExist2(carId, item)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("locateType", locateType);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "carId=? and item=?"
                    , new String[]{"" + carId, "" + item});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，T，重载
    public void modifyTer2(int carId, int item, String tNoNew, String position) {
        if (!terExist2(carId, item)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNoNew", tNoNew);
        contentValues.put("position", position);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "carId=? and item=?"
                    , new String[]{"" + carId, "" + item});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyTer2(int carId, int item, String tNoOld, String tNoNew, String position) {
        if (!terExist2(carId, item)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNoOld", tNoOld);
        contentValues.put("tNoNew", tNoNew);
        contentValues.put("position", position);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL_2
                    , contentValues
                    , "carId=? and item=?"
                    , new String[]{"" + carId, "" + item});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyTer2(int carId, int item, String tNoOld, String tNoNew, String position, String positionPic, String installPic, int wire) {
        if (!terExist2(carId, item)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNoOld", tNoOld);
        contentValues.put("tNoNew", tNoNew);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("wire", wire);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "carId=? and item=?"
                    , new String[]{"" + carId, "" + item});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyTer2ByTid(int tid, String tNoOld, String tNoNew, String position, String positionPic, String installPic, int carId
            , int locateType, int wire){
        if (!terExist2ByTid(tid)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNoOld", tNoOld);
        contentValues.put("tNoNew", tNoNew);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("carId", carId);
        contentValues.put("locateType", locateType);
        contentValues.put("wire", wire);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "tId=?"
                    , new String[]{"" + tid});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  查，T，重载
    public boolean terExist2(int carId, int item) {
        Cursor cursor = getTer2(carId, item);
        return null != cursor && cursor.moveToFirst();
    }

    //  查，T，重载
    public boolean terExist2ByTid(int tId) {
        Cursor cursor = getTer2ByTid(tId);
        return null != cursor && cursor.moveToFirst();
    }

    //  查，T
    public Cursor getTer2(int carId, int item) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_TERMINAL_2
                    , new String[]{"tNoOld, tNoNew, position, item"
                            + ", positionPic, installPic"
                            + ", model, tId, locateType, carId, wire"}
                    , "carId=? and item=?"
                    , new String[]{"" + carId, "" + item}
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

    //  查，T
    public Cursor getTer2ByTid(int tId) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_TERMINAL_2
                    , new String[]{"tNoOld, tNoNew, position, item"
                            + ", positionPic, installPic"
                            + ", model, tId, locateType, carId, wire"}
                    , "tId=?"
                    , new String[]{("" + tId)}
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

    //  查，T
    public Cursor getTer2ByCarId(int carId) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_TERMINAL_2
                    , new String[]{"tNoOld, tNoNew, position, item"
                            + ", positionPic, installPic"
                            + ", model, tId, locateType, carId, wire"}
                    , "carId=?"
                    , new String[]{("" + carId)}
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

    public int getTer2Tid(int carId, int item) {
        int id = 0;
        Cursor cursor = this.getTer2(carId, item);
        if (null != cursor && cursor.moveToFirst()) {
            id = cursor.getInt(7);
        }
        return id;
    }

    public void modifyTer2InstallPic(int carId, int item, String installPic) {
        if (!terExist2(carId, item)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("installPic", installPic);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL_2
                    , contentValues
                    , "carId=? and item=?"
                    , new String[]{"" + carId, "" + item});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyTer2PositionPic(int carId, int item, String positionPic) {
        if (!terExist2(carId, item)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("positionPic", positionPic);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL_2
                    , contentValues
                    , "carId=? and item=?"
                    , new String[]{"" + carId, "" + item});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void deleteTer2ByTid(int tid){
        if (!terExist2ByTid(tid)) {
            return;
        }
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(Data.DATA_TAB_INSTALL_TERMINAL_2
                    , "tId=?"
                    , new String[]{"" + tid});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void close() {
        mSqLiteDatabase.close();
    }
}
