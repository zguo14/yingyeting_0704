package com.example.demo.web;

import cn.hutool.cron.CronException;
import com.example.demo.hk.ClientDemo.InstanceService;
import com.example.demo.hk.ClientDemo.InstanceServiceImpl;
import com.example.demo.hk.entity.CapturePicRequestParam;
import com.example.demo.hk.entity.FileRequest;
import com.example.demo.hk.entity.Instance;
import com.sun.jna.NativeLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.tags.EditorAwareTag;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@Component
public class VideoFrameSchedulerController {
    @Autowired
    InstanceService instanceService;
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
            for (int i = 0; i < frequency; i++) {
                Timer timer = new Timer();
                VideoFrameTaskController task = new VideoFrameTaskController();
                CapturePicRequestParam param = new CapturePicRequestParam();
                param.setIp("192.168.50.26");
                param.setPort("554");
                param.setAccount("admin");
                param.setPassword("123456");
                param.setChannel(new NativeLong(1));
                param.setInstanceID(fr.getId());
                param.setTaskType(fr.getJobType());
                StringBuffer path = new StringBuffer();
                path.append("C:\\Users\\luoyang\\Desktop\\test_demo\\test_demo");
                path.append("\\");
                path.append(fr.getJobType());
                path.append("\\");
                path.append(fr.getLocationId());
                path.append("\\");
                path.append("Image");
                path.append(i);
                path.append(".jpg");
                param.setPath(path.toString());
                task.setParam(param);
                task.setTime(start.toString());
                if (i == 0) {
                    task.setFlag("start");
                } else if (i == frequency - 1) {
                    task.setFlag("end");
                } else {
                    task.setFlag("middle");
                }
                int id = param.getInstanceID();
                List<Instance> inss = instanceService.getInstanceById(id);
                Instance ins = inss.get(0);
                task.setInstance(ins);
                timer.schedule(task, start);
                System.out.println("Task scheduled, instance id: " + ins.getId() + " , start time:" + start);
                start = new Date((start.getTime() / 1000 + interval) * 1000);
            }
        }

    }

}
