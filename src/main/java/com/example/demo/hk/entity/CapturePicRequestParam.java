package com.example.demo.hk.entity;

import org.springframework.stereotype.Component;

@Component
public class CapturePicRequestParam extends Device {
    private int instanceID;
    private String taskType;
    private String path;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
