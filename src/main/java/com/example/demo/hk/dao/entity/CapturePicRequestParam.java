package com.example.demo.hk.dao.entity;

import com.sun.jna.NativeLong;
import org.springframework.stereotype.Component;

@Component
public class CapturePicRequestParam extends Device {
    private int instanceID;
    private String taskType;
    private String directory;
    private NativeLong channel;
    private String jobname;

    @Override
    public NativeLong getChannel() {
        return channel;
    }

    @Override
    public void setChannel(NativeLong channel) {
        this.channel = channel;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

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
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
