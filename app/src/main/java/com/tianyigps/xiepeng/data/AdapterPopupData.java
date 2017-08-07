package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/8/7.
 */

public class AdapterPopupData {
    private String title;
    private int count;

    public AdapterPopupData(String title, int count) {
        this.title = title;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
