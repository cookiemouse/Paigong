package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/8/10.
 */

public class AdapterPendDetailsData {
    private int imageResources;
    private String title, content;

    private boolean isRed;

    public AdapterPendDetailsData(int imageResources, String title, String content) {
        this.imageResources = imageResources;
        this.title = title;
        this.content = content;
    }

    public AdapterPendDetailsData(int imageResources, String title, String content, boolean isRed) {
        this.imageResources = imageResources;
        this.title = title;
        this.content = content;
        this.isRed = isRed;
    }

    public int getImageResources() {
        return imageResources;
    }

    public void setImageResources(int imageResources) {
        this.imageResources = imageResources;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }
}
