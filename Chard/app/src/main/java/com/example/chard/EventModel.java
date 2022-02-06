package com.example.chard;

import java.time.LocalDateTime;

public class EventModel {

    private String event_name;
    private LocalDateTime start_time;
    private LocalDateTime end_time;

    // Constructor
    public EventModel(String event_name, LocalDateTime start_time, LocalDateTime end_time) {
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

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }
}
