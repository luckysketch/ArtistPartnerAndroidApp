package com.appbygourav.gridtimeanalysisapp;

public class SessionDetails {
    int session_id;
    int project_id,minutes;
    String date;

    public SessionDetails(int id, int minutes, String date) {
        this.project_id = id;
        this.minutes = minutes;
        this.date = date;
    }

    public SessionDetails() {
    }

    @Override
    public String toString() {
        int min=minutes/60;
        int seconds=minutes%60;
        int hour=min/60;
        min=min%60;
        if(hour<1){
            return " "+min+" min on "+date;
        }
        else
            return  " "+hour+" hour "+min+" min on "+date;

    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getId() {
        return project_id;
    }

    public void setId(int id) {
        this.project_id = id;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
