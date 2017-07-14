package com.tianyigps.xiepeng.data;

/**
 * Created by djc on 2017/7/14.
 */

public class AdapterStatisticsWorkderData {
    private String type;
    private int number;

    public AdapterStatisticsWorkderData(String type, int number) {
        this.type = type;
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
