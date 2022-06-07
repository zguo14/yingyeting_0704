package com.example.demo.hk.entity;

public class VideoFrame {
//    private boolean hasPrev;
////    private boolean hasNext;
    private String flag;
    private int instanceId;

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
    private String fileTime;

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

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
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
