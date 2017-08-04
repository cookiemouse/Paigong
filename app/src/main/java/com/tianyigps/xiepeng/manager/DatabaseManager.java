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
        addRepair(idMain, null, null, null, null, null, null, null, null, 0);
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
            , String newtNo, String positionUrl, String installUrl, int model) {
        if (repairExist(idMain)) {
            modifyRepair(idMain, tNo, position, positionPic, installPic, explain, newtNo, positionUrl, installUrl);
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("idMain", idMain);
        contentValues.put("tNo", tNo);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);
        contentValues.put("newtNo", newtNo);
        contentValues.put("positionUrl", positionUrl);
        contentValues.put("installUrl", installUrl);
        contentValues.put("model", model);

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
        addRepair(idMain, null, null, null, null, null, null, null, null, model);
    }

    //  增，维修
    public void addRepair(int idMain, String position, String explain) {
        addRepairPosition(idMain, position);
        addRepairExplain(idMain, explain);
    }

    //  增，维修
    public void addRepair(int idMain, String position, String explain, String newtNo) {
        addRepairPosition(idMain, position);
        addRepairExplain(idMain, explain);
        addRepairNewtNo(idMain, newtNo);
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
    public void addRepairPositionPic(int idMain, String positionPic) {
        if (repairExist(idMain)) {
            modifyRepairPositionPic(idMain, positionPic);
            return;
        }
        this.addRepair(idMain);
        this.addRepairPositionPic(idMain, positionPic);
    }

    //  增，维修，重载
    public void addRepairInstallPic(int idMain, String installPic) {
        if (repairExist(idMain)) {
            modifyRepairInstallPic(idMain, installPic);
            return;
        }
        this.addRepair(idMain);
        this.addRepairInstallPic(idMain, installPic);
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
    public void addRepairNewtNo(int idMain, String newtNo) {
        if (repairExist(idMain)) {
            modifyRepairNewtNo(idMain, newtNo);
            return;
        }
        this.addRepair(idMain);
        this.addRepairNewtNo(idMain, newtNo);
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
                             String newtNo, String positionUrl, String installUrl) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNo", tNo);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("explain", explain);
        contentValues.put("newtNo", newtNo);
        contentValues.put("positionUrl", positionUrl);
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
    public void modifyRepairPositionPic(int idMain, String positionPic) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("positionPic", positionPic);

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
    public void modifyRepairInstallPic(int idMain, String installPic) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("installPic", installPic);

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
    public void modifyRepairNewtNo(int idMain, String newtNo) {
        if (!repairExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("newtNo", newtNo);

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
                    , new String[]{"idMain, tNo, position, positionPic, installPic, explain, newtNo" +
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
                    , new String[]{"idMain, tNo, position, positionPic, installPic, explain, newtNo" +
                            ", positionUrl, installUrl, model"}
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

    //=====================维修========================华丽的分割线=======================车辆============================

    //  添加Car
    public void addCar(int idMain) {
        if (carExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("idMain", idMain);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(Data.DATA_TAB_INSTALL_CAR, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase insert error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  添加,car,重载
    public void addCarInfo(int idMain, String carNo, String carType) {
        this.addCarInfo(idMain, carNo, null, carType);
    }

    //  添加,car,重载
    public void addCarInfo(int idMain, String carNo, String frameNo, String carType) {
        if (carExist(idMain)) {
            modifyCar(idMain, carNo, frameNo, carType);
            return;
        }
        this.addCar(idMain);
        this.addCarInfo(idMain, carNo, frameNo, carType);
    }

    //  添加，car，重载
    public void addCarNoPic(int idMain, String carNoPic, String carNoPicUri) {
        if (carExist(idMain)) {
            modifyCarNoPic(idMain, carNoPic, carNoPicUri);
            return;
        }
        this.addCar(idMain);
        this.addCarNoPic(idMain, carNoPic, carNoPicUri);
    }

    //  添加，car，重载
    public void addCarFrameNoPic(int idMain, String frameNoPic, String frameNoPicUri) {
        if (carExist(idMain)) {
            modifyCarFrameNoPic(idMain, frameNoPic, frameNoPicUri);
            return;
        }
        this.addCar(idMain);
        this.addCarFrameNoPic(idMain, frameNoPic, frameNoPicUri);
    }

    //  添加，car，重载
    public void addCarPics(int idMain, int position, String pic) {
        if (carExist(idMain)) {
            modifyCarPics(idMain, position, pic);
            return;
        }
        this.addCar(idMain);
        this.addCarPics(idMain, position, pic);
    }

    //  添加，car，重载
    public void addCarPics(int idMain, int position, String pic, String picUrl) {
        if (carExist(idMain)) {
            modifyCarPics(idMain, position, pic, picUrl);
            return;
        }
        this.addCar(idMain);
        this.addCarPics(idMain, position, pic, picUrl);
    }

    //  删除Car
    public void deleteCar(int idMain) {
        if (!carExist(idMain)) {
            return;
        }
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(Data.DATA_TAB_INSTALL_CAR
                    , "idMain=?"
                    , new String[]{("" + idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  修改，车辆
    public void modifyCar(int idMain, String carNo, String frameNo, String carType
            , String carNoPic, String frameNoPic
            , String pic1, String pic2, String pic3, String pic4, String pic5, String pic6
            , String carNoPicUri, String frameNoPicUri
            , String pic1Uri, String pic2Uri, String pic3Uri, String pic4Uri, String pic5Uri, String pic6Uri) {
        if (!carExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carNo", carNo);
        contentValues.put("frameNo", frameNo);
        contentValues.put("carType", carType);
        contentValues.put("carNoPic", carNoPic);
        contentValues.put("frameNoPic", frameNoPic);
        contentValues.put("pic1", pic1);
        contentValues.put("pic2", pic2);
        contentValues.put("pic3", pic3);
        contentValues.put("pic4", pic4);
        contentValues.put("pic5", pic5);
        contentValues.put("pic6", pic6);
        contentValues.put("carNoPicUri", carNoPicUri);
        contentValues.put("frameNoPicUri", frameNoPicUri);
        contentValues.put("pic1Uri", pic1Uri);
        contentValues.put("pic2Uri", pic2Uri);
        contentValues.put("pic3Uri", pic3Uri);
        contentValues.put("pic4Uri", pic4Uri);
        contentValues.put("pic5Uri", pic5Uri);
        contentValues.put("pic6Uri", pic6Uri);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR
                    , contentValues
                    , "idMain=?"
                    , new String[]{("" + idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  修改，车辆，重载
    public void modifyCar(int idMain, String carNo, String frameNo, String carType) {
        if (!carExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carNo", carNo);
        contentValues.put("frameNo", frameNo);
        contentValues.put("carType", carType);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR
                    , contentValues
                    , "idMain=?"
                    , new String[]{("" + idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  修改，车辆，重载
    public void modifyCarNoPic(int idMain, String carNoPic, String carNoPicUri) {
        if (!carExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("carNoPic", carNoPic);
        contentValues.put("carNoPicUri", carNoPicUri);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR
                    , contentValues
                    , "idMain=?"
                    , new String[]{("" + idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  修改，车辆，重载
    public void modifyCarFrameNoPic(int idMain, String frameNoPic, String frameNoPicUri) {
        if (!carExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("frameNoPic", frameNoPic);
        contentValues.put("frameNoPicUri", frameNoPicUri);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR
                    , contentValues
                    , "idMain=?"
                    , new String[]{("" + idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  修改，car，重载
    public void modifyCarPics(int idMain, int position, String pic) {
        if (!carExist(idMain)) {
            return;
        }

        String key = "pic" + (position + 1);

        ContentValues contentValues = new ContentValues();
        contentValues.put(key, pic);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR
                    , contentValues
                    , "idMain=?"
                    , new String[]{("" + idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  修改，car，重载
    public void modifyCarPics(int idMain, int position, String pic, String picUrl) {
        if (!carExist(idMain)) {
            return;
        }

        String key = "pic" + (position + 1);
        String keyUrl = key + "Uri";

        ContentValues contentValues = new ContentValues();
        contentValues.put(key, pic);
        contentValues.put(keyUrl, picUrl);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_CAR
                    , contentValues
                    , "idMain=?"
                    , new String[]{("" + idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  查，车辆
    public Cursor getCar(int idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_CAR
                    , new String[]{"idMain, carNo, frameNo, carType"
                            + ", carNoPic, frameNoPic"
                            + ", carNoPicUri, frameNoPicUri"
                            + ", pic1, pic2, pic3, pic4, pic5, pic6"
                            + ", pic1Uri, pic2Uri, pic3Uri, pic4Uri, pic5Uri, pic6Uri"}
                    , "idMain=?"
                    , new String[]{("" + idMain)}
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

    //  查，car，RecyclerPic
    public Cursor getCarPics(int idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_CAR
                    , new String[]{"pic1, pic2, pic3, pic4, pic5, pic6"
                            + ", pic1Uri, pic2Uri, pic3Uri, pic4Uri, pic5Uri, pic6Uri"}
                    , "idMain=?"
                    , new String[]{("" + idMain)}
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

    //  查，是否存在该Car
    public boolean carExist(int idMain) {
        Cursor cursor = getCar(idMain);
        return null != cursor && cursor.moveToFirst();
    }

    //=====================车辆========================华丽的分割线=======================设备============================

    //  增，T
    public void addTer(String idMain) {
        if (terExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("idMain", idMain);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.insert(Data.DATA_TAB_INSTALL_TERMINAL, null, contentValues);
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase insert error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  增, T, info
    public void addTerInfo(String idMain, String tNoNew, String position) {
        if (terExist(idMain)) {
            modifyTer(idMain, tNoNew, position);
            return;
        }
        this.addTer(idMain);
        this.addTerInfo(idMain, tNoNew, position);
    }

    //  增, T, info
    public void addTerInfo(String idMain, String tNoOld, String tNoNew, String position) {
        if (terExist(idMain)) {
            modifyTer(idMain, tNoOld, tNoNew, position);
            return;
        }
        this.addTer(idMain);
        this.addTerInfo(idMain, tNoOld, tNoNew, position);
    }

    //  增，T, tId
    public void addTerId(String idMain, int tId) {
        if (terExist(idMain)) {
            modifyTerId(idMain, tId);
            return;
        }
        this.addTer(idMain);
        this.addTerId(idMain, tId);
    }

    //  增，T，PositionPic
    public void addTerPositionPic(String idMain, String positionPic, String positionPicUri) {
        if (terExist(idMain)) {
            modifyTerPositionPic(idMain, positionPic, positionPicUri);
            return;
        }
        this.addTer(idMain);
        this.addTerPositionPic(idMain, positionPic, positionPicUri);
    }

    //  增，T，InstallPic
    public void addTerInstallPic(String idMain, String installPic, String installPicUri) {
        if (terExist(idMain)) {
            modifyTerInstallPic(idMain, installPic, installPicUri);
            return;
        }
        this.addTer(idMain);
        this.addTerInstallPic(idMain, installPic, installPicUri);
    }

    //  删，T
    public void deleteTer(String idMain) {
        if (!terExist(idMain)) {
            return;
        }
        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.delete(Data.DATA_TAB_INSTALL_TERMINAL
                    , "idMain=?"
                    , new String[]{(idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase delete error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，T
    public void modifyTer(String idMain, String tNoOld, String tNoNew, String position
            , String positionPic, String installPic, String positionPicUri, String installPicUri) {
        if (!terExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNoOld", tNoOld);
        contentValues.put("tNoNew", tNoNew);
        contentValues.put("position", position);
        contentValues.put("positionPic", positionPic);
        contentValues.put("installPic", installPic);
        contentValues.put("positionPicUri", positionPicUri);
        contentValues.put("installPicUri", installPicUri);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "idMain=?"
                    , new String[]{(idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，T，重载
    public void modifyTer(String idMain, String tNoNew, String position) {
        if (!terExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNoNew", tNoNew);
        contentValues.put("position", position);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "idMain=?"
                    , new String[]{(idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    public void modifyTerId(String idMain, int tId) {
        if (!terExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tId", tId);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "idMain=?"
                    , new String[]{(idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，T，重载
    public void modifyTer(String idMain, String tNoOld, String tNoNew, String position) {
        if (!terExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("tNoOld", tNoOld);
        contentValues.put("tNoNew", tNoNew);
        contentValues.put("position", position);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "idMain=?"
                    , new String[]{(idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，T，重载
    public void modifyTerPositionPic(String idMain, String positionPic, String positionPicUri) {
        if (!terExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("positionPic", positionPic);
        contentValues.put("positionPicUri", positionPicUri);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "idMain=?"
                    , new String[]{(idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  改，T，重载
    public void modifyTerInstallPic(String idMain, String installPic, String installPicUri) {
        if (!terExist(idMain)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("installPic", installPic);
        contentValues.put("installPicUri", installPicUri);

        mSqLiteDatabase.beginTransaction();
        try {
            mSqLiteDatabase.update(Data.DATA_TAB_INSTALL_TERMINAL
                    , contentValues
                    , "idMain=?"
                    , new String[]{(idMain)});
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, e + "SqLiteDatabase update error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    //  查，T
    public Cursor getTer(String idMain) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_INSTALL_TERMINAL
                    , new String[]{"idMain, tNoOld, tNoNew, position"
                            + ", positionPic, installPic"
                            + ", positionPicUri, installPicUri"}
                    , "idMain=?"
                    , new String[]{(idMain)}
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

    //  查，T，重载
    public boolean terExist(String idMain) {
        Cursor cursor = getTer(idMain);
        return null != cursor && cursor.moveToFirst();
    }

    public void close() {
        mSqLiteDatabase.close();
    }
}
