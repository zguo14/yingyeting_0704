package com.example.demo.web;

import com.example.demo.hk.ClientDemo.HCNetTools;
import com.example.demo.hk.ClientDemo.InstanceService;
import com.example.demo.hk.dao.entity.CapturePicRequestParam;
import com.example.demo.hk.dao.entity.Instance;
import com.example.demo.hk.dao.entity.VideoFrame;
import com.example.demo.hk.redis.RedisServiceImpl;
import com.sun.jna.NativeLong;
import org.springframework.stereotype.Component;
import redis.clients.jedis.StreamEntryID;
import java.sql.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class VideoFrameServiceController {
    // 定时任务的instance为多个实例，需要手动注入
    InstanceService instanceService = BeanUtils.getBean(InstanceService.class);
    public static int pre = -1;

    public int catchFrame(CapturePicRequestParam param, NativeLong lRealHandle, Instance instance, Date time, String flag)  {
        HCNetTools tool = new HCNetTools();
        if (tool.getDVRPic(param, lRealHandle) > 0) {
//        if (1 > 0) {
            VideoFrame frame = new VideoFrame();
            frame.setFlag(flag);
            frame.setInstanceId(param.getInstanceID());
            frame.setTaskType(param.getTaskType());
//            Date date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(time);
            frame.setFileTime(time);
            frame.setFilePath(param.getPath());
            frame.setJobname(param.getJobname());
            if (offerFrameToQueue(frame) < 0 ) {
                System.out.println("offer to queue err");
                return -2;
            } else {
                Instance newInstance = new Instance();
                newInstance.setId(instance.getId());
                newInstance.setJobName(instance.getJobName());
                newInstance.setTaskType(instance.getTaskType());
                newInstance.setJobClass(instance.getJobClass());
                newInstance.setCronExpression(instance.getCronExpression());
                newInstance.setUserId(instance.getUserId());
                newInstance.setLocationId(instance.getLocationId());
                newInstance.setCameraId(instance.getCameraId());
                newInstance.setFileId(instance.getFileId());
                newInstance.setCreateTime(instance.getCreateTime());
                newInstance.setUpdateTime(instance.getUpdateTime());
                newInstance.setIsDelete(instance.getIsDelete());
                newInstance.setCatchId(instance.getCatchId());
                newInstance.setResultDesc(instance.getResultDesc());
                if (flag.equals("start")) {
                    newInstance.setStatus(1);
                    newInstance.setIsLatest(1);
                    pre = instanceService.insert(newInstance);
                    int id = instanceService.getInstanceList(instance.getJobName(), instance.getIsLatest()).get(0).getId();
//                    instanceService.update(instance.getId());
                    instanceService.update(id);
                    System.out.println("-----------Instance:"+ instance.getId() + ", First frame inserted and updated--------------");
                }
                if (flag.equals("end")) {
                    newInstance.setStatus(2);
                    newInstance.setIsLatest(1);
                    int id = instanceService.getInstanceList(instance.getJobName(), instance.getIsLatest()).get(0).getId();
                    instanceService.update(id);
//                    instanceService.update(pre);
                    pre = instanceService.insert(newInstance);
                    System.out.println("-----------Instance:"+ instance.getId() + ", Last frame inserted and updated--------------");
                }
            }
            return 1;
        } else {
            return -1;
        }
    }

    public int offerFrameToQueue(VideoFrame frame) {
        RedisServiceImpl redis = new RedisServiceImpl();
        redis.init();
        Map<String, String> map = new HashMap<>();
        map.put("flag", frame.getFlag());
        map.put("instance_id", String.valueOf(frame.getInstanceId()));
        map.put("job_name", frame.getJobname());
        map.put("task_type", frame.getTaskType());
        map.put("file_path", frame.getFilePath());

//        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Timestamp ts = null;
//        try {
//            ts = Timestamp.valueOf(sdf2.format(sdf.parse(frame.getFileTime())));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Timestamp timestamp = new Timestamp(frame.getFileTime().getTime());

        map.put("file_time", timestamp.toString());
        StreamEntryID id = redis.xadd("frame_queue", StreamEntryID.NEW_ENTRY, map);
        System.out.println("successfully offered to queue, instance: "
                + frame.getInstanceId() + ", time point:" + frame.getFileTime()
                + ", file name: " + frame.getFilePath());
        return 1;
    }
}
