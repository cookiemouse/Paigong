package com.tianyigps.xiepeng.data;

/**
 * Created by cookiemouse on 2017/7/24.
 */

public class AdapterInstallingData {
    private String frameNo;
    private int orderLine, orderOffline;
    private int completeLine, completeOffline;

    public AdapterInstallingData(String frameNo, int orderLine, int orderOffline, int completeLine, int completeOffline) {
        this.frameNo = frameNo;
        this.orderLine = orderLine;
        this.orderOffline = orderOffline;
        this.completeLine = completeLine;
        this.completeOffline = completeOffline;
    }

    public String getFrameNo() {
        return frameNo;
    }

    public void setFrameNo(String frameNo) {
        this.frameNo = frameNo;
    }

    public int getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(int orderLine) {
        this.orderLine = orderLine;
    }

    public int getOrderOffline() {
        return orderOffline;
    }

    public void setOrderOffline(int orderOffline) {
        this.orderOffline = orderOffline;
    }

    public int getCompleteLine() {
        return completeLine;
    }

    public void setCompleteLine(int completeLine) {
        this.completeLine = completeLine;
    }

    public int getCompleteOffline() {
        return completeOffline;
    }

    public void setCompleteOffline(int completeOffline) {
        this.completeOffline = completeOffline;
    }
}
