package com.example.demo.hk.dao.entity;

public class Camera {
    private int cameraId;
    private String cameraIp;
    private int locationId;
    private String cameraName;
    private String state;
    private String taskType;
    private String cameraPort;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraPort() {
        return cameraPort;
    }

    public void setCameraPort(String cameraPort) {
        this.cameraPort = cameraPort;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraIp() {
        return cameraIp;
    }

    public void setCameraIp(String cameraIp) {
        this.cameraIp = cameraIp;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return cameraName;
    }

    public void setName(String name) {
        this.cameraName = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }



}
