package com.tianyigps.dispatch2.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tianyigps.dispatch2.data.Data;

/**
 * Created by cookiemouse on 2017/7/26.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int VERSION = 1;
    private static final int VERSION_NEW = 2;

    private static final String TABLE_ORDER = "create table " + Data.DATA_TAB_ORDER
            + "(orderNo TEXT,carId INTEGER,tId INTEGER)";

    private static final String TABLE_REPAIR = "create table " + Data.DATA_TAB_REPAIR
            + "(idMain INTEGER,tNo TEXT,position TEXT,positionPic TEXT,installPic TEXT,explain TEXT" +
            ",newImei TEXT,positionUrl TEXT,installUrl TEXT,model INTEGER,locateType INTEGER" +
            ", wire INTEGER, replace INTEGER)";

    //  拆除似乎不用存本地数据
    private static final String TABLE_REMOVE = "create table " + Data.DATA_TAB_REMOVE
            + "(frameNo TEXT,tNo TEXT,removeCountWire INTEGER,removeCountWireless INTEGER)";

    private static final String TABLE_INSTALL_CAR = "create table " + Data.DATA_TAB_INSTALL_CAR
            + "(idMain INTEGER,carNo TEXT,frameNo TEXT,carType TEXT"
            + ",carNoPic TEXT,frameNoPic TEXT"
            + ",pic1 TEXT,pic2 TEXT,pic3 TEXT,pic4 TEXT,pic5 TEXT,pic6 TEXT"
            + ",carNoPicUri TEXT,frameNoPicUri TEXT"
            + ",pic1Uri TEXT,pic2Uri TEXT,pic3Uri TEXT,pic4Uri TEXT,pic5Uri TEXT,pic6Uri TEXT)";

    private static final String TABLE_INSTALL_TERMINAL = "create table " + Data.DATA_TAB_INSTALL_TERMINAL
            + "(idMain TEXT,tNoOld TEXT,tNoNew TEXT,position TEXT,positionPic TEXT,installPic TEXT" +
            ",positionPicUri TEXT,installPicUri TEXT,model INTEGER,tId INTEGER,locateType INTEGER,carId INTEGER, wire INTEGER)";

    private static final String TABLE_INSTALL_CAR_2 = "create table " + Data.DATA_TAB_INSTALL_CAR_2
            + "(carId INTEGER,carNo TEXT,frameNo TEXT,carType TEXT"
            + ",carNoPic TEXT,frameNoPic TEXT)";

    private static final String TABLE_INSTALL_TERMINAL_2 = "create table " + Data.DATA_TAB_INSTALL_TERMINAL_2
            + "(tNoOld TEXT,tNoNew TEXT,position TEXT,item INTEGER"
            + ",positionPic TEXT,installPic TEXT"
            + ",model INTEGER,tId INTEGER,locateType INTEGER,carId INTEGER, wire INTEGER)";

    /*
    * 需要保存的信息
    *
    * =====总的======
    * //    车辆id  carId int
    * //    设备id  tId int
    *
    * 车架号   frameNo String  1级
    * 设备号   tNo     String  2级
    *
    * ======维修======
    * --2级数据--
    * 安装位置  String
    * 安装位置图  String(path)   图片
    * 接线图   String(path)    图片
    * 检修说明  String
    * 是否替换  boolean
    * 新IMEI     String
    *
    * ======拆除======
    * --1级数据--
    * 拆除数量有线  removeCountWire       int
    * 拆除数量无线  removeCountWireless   int
    *
    * ======安装======
    * --1级数据--
    * 安装数量有线    installCountWire        int
    * 安装数量无线    installCountWireless    int
    *
    * 车牌号   String(path)    图片
    * 车架号   String(path)    图片
    * 车辆图1   String(path)    图片
    * 车辆图2   String(path)    图片
    * 车辆图3   String(path)    图片
    * 车辆图4   String(path)    图片
    * 车辆图5   String(path)    图片
    * 车辆图6   String(path)    图片
    *
    * --2级数据--
    * 设备号   tNo(imei)   String
    * 设备类型  tType   int
    * 安装位置  String  文字
    * 安装位置  String(path)    图片
    * 接线图   String(path)    图片
    *
    *
    * **/

    public DatabaseHelper(Context context, String name) {
        this(context, name, null, VERSION_NEW);
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate: -->" + 1);
        sqLiteDatabase.execSQL(TABLE_ORDER);
        sqLiteDatabase.execSQL(TABLE_REPAIR);
//        sqLiteDatabase.execSQL(TABLE_INSTALL_CAR);
//        sqLiteDatabase.execSQL(TABLE_INSTALL_TERMINAL);
        sqLiteDatabase.execSQL(TABLE_INSTALL_CAR_2);
        sqLiteDatabase.execSQL(TABLE_INSTALL_TERMINAL_2);
        Log.i(TAG, "onCreate: -->" + 2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: ");
        if (oldVersion == VERSION && newVersion == VERSION_NEW) {
            Log.i(TAG, "onUpgrade: -->2");
            sqLiteDatabase.execSQL(TABLE_INSTALL_CAR_2);
            sqLiteDatabase.execSQL(TABLE_INSTALL_TERMINAL_2);
        }
    }
}
