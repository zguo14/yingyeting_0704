package com.example.demo.hk.dao.entity;

public class CameraTask {
    private int id;
    private int cameraId;
    private String taskType;
    private int channel;
    private String rtspCmd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getRtspCmd() {
        return rtspCmd;
    }

    public void setRtspCmd(String rtspCmd) {
        this.rtspCmd = rtspCmd;
    }
}
