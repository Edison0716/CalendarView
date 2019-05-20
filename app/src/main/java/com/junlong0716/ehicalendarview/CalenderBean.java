package com.junlong0716.ehicalendarview;

import com.junlong0716.pickerview.DayBaseEntity;

import java.util.List;

/**
 * 实体类
 */
public class CalenderBean {
    private int month;
    private int year;
    private int setCheckedDay;
    private List<Day> days;
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

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public static class Day extends DayBaseEntity{

    }
}
