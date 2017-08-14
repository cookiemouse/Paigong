package com.tianyigps.dispatch2.data;

/**
 * Created by djc on 2017/7/13.
 */

public class AdapterMineData {
    private int imageid;
    private String title;

    public AdapterMineData(int imageid, String title) {
        this.imageid = imageid;
        this.title = title;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
