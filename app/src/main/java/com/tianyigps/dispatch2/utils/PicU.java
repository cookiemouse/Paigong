package com.tianyigps.dispatch2.utils;

import android.media.ExifInterface;
import android.util.Log;

import com.tianyigps.dispatch2.data.PicData;

import java.io.IOException;

/**
 * Created by cookiemouse on 2018/2/1.
 */

public class PicU {

    private static final String TAG = "PicU";

    public static PicData getExinfo(String path) {
        Log.i(TAG, "getExinfo: path-->" + path);

        try {
            ExifInterface exifInterface = new ExifInterface(path);

            String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            Log.i(TAG, "getExinfo: orientation-->" + orientation);
            String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            Log.i(TAG, "getExinfo: dateTime-->" + dateTime);
            String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            Log.i(TAG, "getExinfo: make-->" + make);
            String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            Log.i(TAG, "getExinfo: model-->" + model);
            String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            Log.i(TAG, "getExinfo: flash-->" + flash);
            String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            Log.i(TAG, "getExinfo: imageLength-->" + imageLength);
            String imageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            Log.i(TAG, "getExinfo: imageWidth-->" + imageWidth);
            String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            Log.i(TAG, "getExinfo: latitude-->" + latitude);
            String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            Log.i(TAG, "getExinfo: longitude-->" + longitude);
            String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            Log.i(TAG, "getExinfo: latitudeRef-->" + latitudeRef);
            String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            Log.i(TAG, "getExinfo: longitudeRef-->" + longitudeRef);
            String exposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            Log.i(TAG, "getExinfo: exposureTime-->" + exposureTime);
            String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
            Log.i(TAG, "getExinfo: aperture-->" + aperture);
            String isoSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO);
            Log.i(TAG, "getExinfo: isoSpeedRatings-->" + isoSpeedRatings);
            String dateTimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
            Log.i(TAG, "getExinfo: dateTimeDigitized-->" + dateTimeDigitized);
            String subSecTime = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
            Log.i(TAG, "getExinfo: subSecTime-->" + subSecTime);
            String subSecTimeOrig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIG);
            Log.i(TAG, "getExinfo: subSecTimeOrig-->" + subSecTimeOrig);
            String subSecTimeDig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIG);
            Log.i(TAG, "getExinfo: subSecTimeDig-->" + subSecTimeDig);
            String altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
            Log.i(TAG, "getExinfo: altitude-->" + altitude);
            String altitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
            Log.i(TAG, "getExinfo: altitudeRef-->" + altitudeRef);
            String gpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            Log.i(TAG, "getExinfo: gpsTimeStamp-->" + gpsTimeStamp);
            String gpsDateStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            Log.i(TAG, "getExinfo: gpsDateStamp-->" + gpsDateStamp);
            String whiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            Log.i(TAG, "getExinfo: whiteBalance-->" + whiteBalance);
            String focalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            Log.i(TAG, "getExinfo: focalLength-->" + focalLength);
            String processingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
            Log.i(TAG, "getExinfo: processingMethod-->" + processingMethod);

            if (RegularU.isEmpty(latitude)) {
                latitude = "0.0";
            } else {
                try {
                    latitude = "" + convertToDegree(latitude);
                } catch (Exception e) {
                    Log.i(TAG, "getExinfo: e-->" + e);
                    latitude = "0.0";
                }
            }

            if (RegularU.isEmpty(longitude)) {
                longitude = "0.0";
            } else {
                try {
                    longitude = "" + convertToDegree(longitude);
                } catch (Exception e) {
                    Log.i(TAG, "getExinfo: e-->" + e);
                    longitude = "0.0";
                }
            }

            if (RegularU.isEmpty(dateTime)) {
                dateTime = new TimeFormatU().millisToDate(System.currentTimeMillis());
            } else {
                dateTime = TimeFormatU.dateToDate(dateTime);
            }

            return new PicData(dateTime, latitude, longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PicData(new TimeFormatU().millisToDate(System.currentTimeMillis()), "0.0", "0.0");
    }

    public static Double convertToDegree(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        Log.i(TAG, "convertToDegree: result-->" + (double) result);

        return (double) result;
    }
}
