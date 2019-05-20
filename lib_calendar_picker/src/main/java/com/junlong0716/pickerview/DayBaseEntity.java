package com.junlong0716.pickerview;

/**
 * FileName: MonthBaseEntity
 * Author:   EdisonLi的Windows
 * Date:     2019/5/20 9:49
 * Description:
 */
public class DayBaseEntity {
    private int day;
    private String des;
    private boolean isDisable;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }
}
