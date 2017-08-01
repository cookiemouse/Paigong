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
    public void addRepair(String tNo, String position, String positionPic, String installPic, String explain, String newtNo,
                          String positionUrl, String installUrl) {
        if (repairExist(tNo)) {
            modifyRepair(tNo, position, positionPic, installPic, explain, newtNo, positionUrl, installUrl);
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
        addRepair(tNo, position, null, null, null, null, null, null);
    }

    //  增，维修，重载
    public void addRepairPositionPic(String tNo, String positionPic) {
        if (repairExist(tNo)) {
            modifyRepairPositionPic(tNo, positionPic);
            return;
        }
        addRepair(tNo, null, positionPic, null, null, null, null, null);
    }

    //  增，维修，重载
    public void addRepairInstallPic(String tNo, String installPic) {
        if (repairExist(tNo)) {
            modifyRepairInstallPic(tNo, installPic);
            return;
        }
        addRepair(tNo, null, null, installPic, null, null, null, null);
    }

    //  增，维修，重载
    public void addRepairExplain(String tNo, String explain) {
        if (repairExist(tNo)) {
            modifyRepairExplain(tNo, explain);
            return;
        }
        addRepair(tNo, null, null, null, explain, null, null, null);
    }

    //  增，维修，重载
    public void addRepairNewtNo(String tNo, String newtNo) {
        if (repairExist(tNo)) {
            modifyRepairNewtNo(tNo, newtNo);
            return;
        }
        addRepair(tNo, null, null, null, null, newtNo, null, null);
    }

    //  增，维修，重载
    public void addRepairPositionUrl(String tNo, String positionUrl) {
        if (repairExist(tNo)) {
            modifyRepairPositionUrl(tNo, positionUrl);
            return;
        }
        addRepair(tNo, null, null, null, null, null, positionUrl, null);
    }

    //  增，维修，重载
    public void addRepairInstallUrl(String tNo, String installUrl) {
        if (repairExist(tNo)) {
            modifyRepairInstallUrl(tNo, installUrl);
            return;
        }
        addRepair(tNo, null, null, null, null, null, null, installUrl);
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
    public void modifyRepair(String tNo, String position, String positionPic, String installPic, String explain, String newtNo,
                             String positionUrl, String installUrl) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
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
    public void modifyRepairNewtNo(String tNo, String newtNo) {
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

    //  改，维修，重载
    public void modifyRepairPositionUrl(String tNo, String positionUrl) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("positionUrl", positionUrl);

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
    public void modifyRepairInstallUrl(String tNo, String installUrl) {
        if (!repairExist(tNo)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("installUrl", installUrl);

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
                    , new String[]{"tNo, position, positionPic, installPic, explain, newtNo, positionUrl, installUrl"}
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
                    , new String[]{"tNo, position, positionPic, installPic, explain, newtNo, positionUrl, installUrl"}
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

    //  查，维修，重载
    public String getRepairPositionPic(String tNo) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"positionPic"}
                    , "tNo=?"
                    , new String[]{tNo}
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
    public String getRepairInstallPic(String tNo) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"installPic"}
                    , "tNo=?"
                    , new String[]{tNo}
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
    public String getRepairPositionUrl(String tNo) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"positionUrl"}
                    , "tNo=?"
                    , new String[]{tNo}
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
    public String getRepairInstallUrl(String tNo) {
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_REPAIR
                    , new String[]{"installUrl"}
                    , "tNo=?"
                    , new String[]{tNo}
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
    public boolean repairExist(String tNo) {
        Cursor cursor = getRepair(tNo);
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

    public void close() {
        mSqLiteDatabase.close();
    }
}
