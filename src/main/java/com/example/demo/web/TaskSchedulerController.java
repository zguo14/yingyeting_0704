package com.example.demo.web;


import com.example.demo.hk.ClientDemo.InstanceService;
import com.example.demo.hk.entity.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

//@Controller
//@EnableScheduling
//public class TaskSchedulerController {
//    public TaskSchedulerController() {
//        System.out.println("fuck!");
//    }
//    @Autowired
//    InstanceService instanceService;
//    private static final int  i = 2000;
//    @Scheduled(fixedRate = i)
//    public void scheduleTask() {
//        List<Instance> instanceList = instanceService.getInstanceListByOpts(0);
//        VideoFrameScheduler scheduler = new VideoFrameScheduler();
//        scheduler.schedule(instanceList);
//    }
//}
