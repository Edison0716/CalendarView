package com.junlong0716.ehicalendarview;

/**
 * 实体类
 */
public class CalenderBean {
    private int month;
    private int year;
    private int setCheckedDay;
    private String groupName;

    public int getSetCheckedDay() {
        return setCheckedDay;
    }

    public void setSetCheckedDay(int setCheckedDay) {
        this.setCheckedDay = setCheckedDay;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
