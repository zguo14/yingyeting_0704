package com.example.demo.web;

import com.example.demo.hk.ClientDemo.HCNetTools;
import com.example.demo.hk.ClientDemo.InstanceService;
import com.example.demo.hk.ClientDemo.CameraService;
import com.example.demo.hk.dao.entity.Camera;
import com.example.demo.hk.dao.entity.CapturePicRequestParam;
import com.example.demo.hk.dao.entity.FileRequest;
import com.example.demo.hk.dao.entity.Instance;
import com.sun.jna.NativeLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class VideoFrameSchedulerController {
    @Autowired
    InstanceService instanceService;
    @Autowired
    CameraService cameraService;
    int frequency = 10;
    public void schedule(List<FileRequest> fileRequestsList) {
        for (FileRequest fr : fileRequestsList) {
            StringBuffer date = new StringBuffer();
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.lang.String nowymd = sdf.format(now.getTime());
            date.append(nowymd);
            date.append(" ");
            String nowhms = fr.getBeginH() + ":" + fr.getBeginM() + ":" + fr.getBeginS();
            date.append(fr.getBeginH() + ":");
            date.append(fr.getBeginM() + ":");
            date.append(fr.getBeginS());

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = null;
            try {
                start = sdf2.parse(date.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Long duration = Long.parseLong(fr.getIntervalH()) * 3600 +
                    Long.parseLong(fr.getIntervalM()) * 60 + Long.parseLong(fr.getIntervalS());
            Long interval = duration / frequency;
            List<NativeLong> handle = new ArrayList<>();
            for (int i = 0; i < frequency; i++) {
                Timer timer = new Timer();
                VideoFrameTaskController task = new VideoFrameTaskController();
                CapturePicRequestParam param = new CapturePicRequestParam();
                Camera c = cameraService.getCameraById(Integer.parseInt(String.valueOf(fr.getLocationId())));
                param.setIp(c.getCameraIp());
                param.setPort(c.getCameraPort());
                param.setChannel(new NativeLong(0));
                param.setAccount("admin");
                param.setPassword("yspjd_c608");
                param.setInstanceID(fr.getId());
                param.setTaskType(fr.getJobType());
                param.setJobname(fr.getJobName());
                StringBuffer path = new StringBuffer();
                // debug0627
                path.append("C:\\Users\\luoyang\\Desktop\\test_demo\\test_demo\\environment_demo\\env_demo_17");
                path.append("\\");
                path.append("Image");
                path.append(i);
                path.append(".jpg");
                param.setPath(path.toString());
                task.setParam(param);
                task.setTime(start);
                if (i == 0) {
                    task.setFlag("start");
                    HCNetTools tool = new HCNetTools();
                    handle = tool.initDVR(param); // 抓取首桢时登录、预览，获取两个句柄
                } else if (i == frequency - 1) {
                    task.setFlag("end");
                } else {
                    task.setFlag("middle");
                }
                task.setHandle(handle);
//                int id = param.getInstanceID();
//                List<Instance> inss = instanceService.getInstanceById(id);
                String name = fr.getJobName();
                List<Instance> inss = instanceService.getInstanceList(name.substring(0, name.indexOf("_")), 1);
                if (inss == null) {
                    System.out.println("InstanceList is null");
                }
                Instance ins = inss.get(0);
                task.setInstance(ins);
                timer.schedule(task, start);
                System.out.println("Task scheduled, instance id: " + ins.getId() + " , start time:" + start);
                start = new Date((start.getTime() / 1000 + interval) * 1000);
            }
        }

    }

}
