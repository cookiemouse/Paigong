package com.tianyigps.dispatch2.data;

/**
 * Created by cookiemouse on 2017/12/8.
 */

public class AdapterLocateWarnData {
    private String type;
    private String time;

    public AdapterLocateWarnData(String type, String time) {
        this.type = type;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
