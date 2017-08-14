package com.tianyigps.dispatch2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by djc on 2017/7/17.
 */

public class TimeFormatU {

    public String millisToDate(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public String millisToColock(long mills) {
//        int hour = (int) (mills / 1000 / 3600);
//        int min = (int) (mills / 1000 % 3600 / 60);
        int min = (int) (mills / 1000 / 60);
        int second = (int) (mills / 1000 % 3600 % 60);
        String time = "";
//        if (hour < 10) {
//            time = "0" + hour;
//        } else {
//            time = "" + hour;
//        }
        if (min < 10) {
            time += "0" + min;
        } else {
            time += "" + min;
        }
        if (second < 10) {
            time += ":0" + second;
        } else {
            time += ":" + second;
        }
        return time;
    }

    //字符串转时间戳
    public long dateToMillis(String time) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            timeStamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }
}