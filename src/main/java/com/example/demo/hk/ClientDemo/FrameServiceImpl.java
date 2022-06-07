//package com.example.demo.hk.ClientDemo;
//
//import com.example.demo.hk.entity.CapturePicRequestParam;
//import com.example.demo.hk.entity.Instance;
//import com.example.demo.hk.entity.VideoFrame;
//import com.example.demo.hk.redis.RedisService;
//import com.example.demo.hk.redis.RedisServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureOrder;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.StreamEntryID;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class FrameServiceImpl implements FrameService {
//
////    @Autowired
////
//    @Autowired
//    InstanceService instanceService;
//
//    @Override
//    public int catchFrame(CapturePicRequestParam param, Instance instance, String time, boolean hasPrev, boolean hasNext) {
////        HCNetTools tool = new HCNetTools();
////        if (tool.getDVRPic(param) > 0) {
//        if (1 > 0) {
//            VideoFrame frame = new VideoFrame();
//            frame.setHasPrev(hasPrev);
//            frame.setHasNext(hasNext);
//            frame.setInstanceId(param.getInstanceID());
//            frame.setTaskType(param.getTaskType());
//            frame.setFileTime(time);
//            frame.setFilePath(param.getPath());
//            if (offerFrameToQueue(frame) < 0 ) {
//                System.out.println("offer to queue err");
//                return -2;
//            } else {
//                Instance newInstance = new Instance();
//                newInstance.setId(instance.getId());
//                newInstance.setTaskType(instance.getTaskType());
//                newInstance.setJobClass(instance.getJobClass());
//                newInstance.setCronExpression(instance.getCronExpression());
//                newInstance.setUserId(instance.getUserId());
//                newInstance.setLocationId(instance.getLocationId());
//                newInstance.setCameraId(instance.getCameraId());
//                newInstance.setFileId(instance.getFileId());
//                newInstance.setCreateTime(instance.getCreateTime());
//                newInstance.setUpdateTime(instance.getUpdateTime());
//                newInstance.setIsDelete(instance.getIsDelete());
//                newInstance.setCatchId(instance.getCatchId());
//                newInstance.setResultDesc(instance.getResultDesc());
//                if (!hasPrev) {
//                    newInstance.setStatus(1);
//                    newInstance.setIsLatest(1);
//                    insertInstance(newInstance);
//                    updateInstance(instance.getId());
//                    System.out.println("insert and update1");
//                }
//                if (!hasNext) {
//                    newInstance.setStatus(2);
//                    newInstance.setIsLatest(1);
//                    insertInstance(newInstance);
//                    updateInstance(instance.getId());
//                    System.out.println("insert and update2");
//                }
//            }
//            return 1;
//        } else {
//            return -1;
//        }
//    }
//
//    @Override
//    public int offerFrameToQueue(VideoFrame frame) {
//        System.out.println("start offering to queue");
//        RedisServiceImpl redis = new RedisServiceImpl();
//        redis.init();
//        Map<String, String> map = new HashMap<>();
//        map.put("hasPrev", String.valueOf(frame.isHasPrev()));
//        map.put("hasNext", String.valueOf(frame.isHasNext()));
//        map.put("instanceId", String.valueOf(frame.getInstanceId()));
//        map.put("taskType", frame.getTaskType());
//        map.put("filePath", frame.getFilePath());
//        map.put("fileTime", frame.getFileTime());
//        StreamEntryID id = redis.xadd("frame_queue", StreamEntryID.NEW_ENTRY, map);
//        System.out.println("successfully offered to queue");
//        return 1;
//    }
//
//    @Override
//    public int updateInstance(int id) {
////        InstanceServiceImpl instanceServiceImpl = new InstanceServiceImpl();
//        return instanceService.update(id);
//    }
//
//    @Override
//    public int insertInstance(Instance instance) {
////        InstanceServiceImpl instanceServiceImpl = new InstanceServiceImpl();
//        return instanceService.insert(instance);
//    }
//
//
//}
