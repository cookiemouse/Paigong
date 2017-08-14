package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2017/8/8.
 */

public class AdapterOrderTrackData {
    private long time;
    private String info;

    public AdapterOrderTrackData(long time, String info) {
        this.time = time;
        this.info = info;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
