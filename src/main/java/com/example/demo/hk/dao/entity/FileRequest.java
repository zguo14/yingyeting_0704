package com.example.demo.hk.dao.entity;

public class FileRequest {
    private int Id;
    private String jobName;
    private String jobType;
    private String jobClass;
    private int status;
    private String cronExpression;
    private Long locationId;
    private int userId;
    private String intervalH;
    private String intervalM;
    private String intervalS;
    private String beginH;
    private String beginM;
    private String beginS;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIntervalH() {
        return intervalH;
    }

    public void setIntervalH(String intervalH) {
        this.intervalH = intervalH;
    }

    public String getIntervalM() {
        return intervalM;
    }

    public void setIntervalM(String intervalM) {
        this.intervalM = intervalM;
    }

    public String getIntervalS() {
        return intervalS;
    }

    public void setIntervalS(String intervalS) {
        this.intervalS = intervalS;
    }

    public String getBeginH() {
        return beginH;
    }

    public void setBeginH(String beginH) {
        this.beginH = beginH;
    }

    public String getBeginM() {
        return beginM;
    }

    public void setBeginM(String beginM) {
        this.beginM = beginM;
    }

    public String getBeginS() {
        return beginS;
    }

    public void setBeginS(String beginS) {
        this.beginS = beginS;
    }
}
