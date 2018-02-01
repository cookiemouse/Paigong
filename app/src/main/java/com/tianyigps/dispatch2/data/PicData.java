package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2018/2/1.
 */

public class PicData {
    private String datetime;
    private String latitude;
    private String longitued;

    public PicData(String datetime, String latitude, String longitued) {
        this.datetime = datetime;
        this.latitude = latitude;
        this.longitued = longitued;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitued() {
        return longitued;
    }

    public void setLongitued(String longitued) {
        this.longitued = longitued;
    }
}
