package com.example.demo.web;

import com.example.demo.hk.entity.CapturePicRequestParam;
import com.example.demo.hk.entity.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class VideoFrameTaskController extends TimerTask {
//    @Autowired
//    VideoFrameServiceController frameService;

    private CapturePicRequestParam param;
    private Instance instance;

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    private String time;
    private boolean hasPrev;
    private boolean hasNext;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public CapturePicRequestParam getParam() {
        return param;
    }

    public void setParam(CapturePicRequestParam param) {
        this.param = param;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }
    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public void run() {
//        FrameServiceImpl frameService = new FrameServiceImpl();
        VideoFrameServiceController frameService = new VideoFrameServiceController();
        frameService.catchFrame(param, instance, time, flag);
    }
}
