package com.example.demo.web;

import com.example.demo.hk.ClientDemo.HCNetTools;
import com.example.demo.hk.dao.entity.CapturePicRequestParam;
import com.example.demo.hk.dao.entity.Instance;
import com.sun.jna.NativeLong;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
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

    private Date time;
    private boolean hasPrev;
    private boolean hasNext;
    private String flag;
    private List<NativeLong> handle;

    public List<NativeLong> getHandle() {
        return handle;
    }

    public void setHandle(List<NativeLong> handle) {
        this.handle = handle;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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
        frameService.catchFrame(param, handle.get(1), instance, time, flag);
        if (flag.equals("end")) {
            HCNetTools tool = new HCNetTools();
            if (tool.resetDVR(handle)) {
                System.out.println("dvr停止预览、退出登录 成功");
            }
        }
    }
}
