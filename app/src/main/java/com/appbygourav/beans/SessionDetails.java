package com.appbygourav.beans;

import com.appbygourav.Service.CommonUtility;

public class SessionDetails {
    private int sessionId;
    private int projectId;
    private int timeSeconds;
    private String date;
    private String modidate;
    private String createdate;


    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getModidate() {
        return modidate;
    }

    public void setModidate(String modidate) {
        this.modidate = modidate;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public SessionDetails(int id, int seconds, String date) {
        this.projectId = id;
        this.timeSeconds = seconds;
        this.date = date;
    }

    public SessionDetails() {
    }

    @Override
    public String toString() {
        String timeString = CommonUtility.getTimeStringFromSeconds(this.timeSeconds);
        String dateString = CommonUtility.getEnglishDateStrFromOldDbFormat(this.date);
        return timeString + " on "+dateString;
    }


}
