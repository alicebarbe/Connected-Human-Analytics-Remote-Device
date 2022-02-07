package com.example.chard;

import com.google.firebase.Timestamp;

import java.util.Date;

public class EventModel {

    private String event_name;
    private Date start_time;
    private Date end_time;

    // Constructor
    public EventModel(String event_name, Date start_time, Date end_time) {
        this.event_name = event_name;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    // Getter and Setter
    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}
