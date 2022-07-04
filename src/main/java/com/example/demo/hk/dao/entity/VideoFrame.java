package com.example.demo.hk.dao.entity;

import java.util.Date;

public class VideoFrame {
//    private boolean hasPrev;
////    private boolean hasNext;
    private String flag;
    private int instanceId;
    private String jobname;

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
//    public boolean isHasNext() {
//        return hasNext;
//    }
//
//    public void setHasNext(boolean hasNext) {
//        this.hasNext = hasNext;
//    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    private String taskType;
    private String filePath;
    private Date fileTime;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getFileTime() {
        return fileTime;
    }

    public void setFileTime(Date fileTime) {
        this.fileTime = fileTime;
    }

//    public boolean isHasPrev() {
//        return hasPrev;
//    }
//
//    public void setHasPrev(boolean hasPrev) {
//        this.hasPrev = hasPrev;
//    }
}
