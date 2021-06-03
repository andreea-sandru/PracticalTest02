package ro.pub.cs.systems.eim.practicaltest02.models;

import java.sql.Time;

public class TimeModel {

    private String hour, time;

    public TimeModel() {

    }

    public TimeModel(String hour, String time) {
        this.time = time;
        this.hour = hour;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
