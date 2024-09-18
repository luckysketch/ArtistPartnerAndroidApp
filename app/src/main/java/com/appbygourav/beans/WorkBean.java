package com.appbygourav.beans;

public class WorkBean {
    private int totalSessions;
    private int totalProject;
    private long totalSeconds;
    private int maxSessionTime;

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public int getTotalProject() {
        return totalProject;
    }

    public void setTotalProject(int totalProject) {
        this.totalProject = totalProject;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public int getMaxSessionTime() {
        return maxSessionTime;
    }

    public void setMaxSessionTime(int maxSessionTime) {
        this.maxSessionTime = maxSessionTime;
    }

    public long getMaxProjectTime() {
        return maxProjectTime;
    }

    public void setMaxProjectTime(long maxProjectTime) {
        this.maxProjectTime = maxProjectTime;
    }

    private long maxProjectTime;
}
