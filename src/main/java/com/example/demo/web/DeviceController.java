package com.example.demo.web;

import com.example.demo.hk.ClientDemo.*;
import com.example.demo.hk.dao.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static cn.hutool.core.net.NetUtil.ping;

@Controller
@RequestMapping("/dev")
@EnableScheduling
public class DeviceController {

	@Autowired
	InstanceService instanceService;

	@Autowired
	FileRequestService fileRequestService;

	@Autowired
	VideoFrameSchedulerController scheduler;

	@Autowired
	CameraService cameraService;

//	@Scheduled(fixedRate = 3600000)
	public void updateCameraStatus() {
		List<Camera> list = cameraService.getCameraList();
		HCNetTools hcTool = new HCNetTools();
		for (Camera c : list) {
			cameraService.updateStatus(c.getCameraId(), hcTool.checkStatus(c.getCameraIp(), Integer.parseInt(c.getCameraPort())));
		}
	}

	// debug0627
	@Scheduled(fixedRate = 1000000000)
//	@Scheduled(cron = "0 0 0 * * ?")
//	@Scheduled(cron = "0 0 4 * * ?")
	public void scheduleTask() {
		List<FileRequest> fileRequestList = fileRequestService.getFileRequestListByOpts(0);
		scheduler.schedule(fileRequestList);
	}

//	@Scheduled(fixedRate = 1000000000)
//	@Scheduled(cron = "0 0 1 * * ?")
//	@Scheduled(cron = "0 0 7 * * ?")
//	public void copy() {
//		 List<Instance> instanceList = instanceService.getInstanceListByStatus(0);
//		 for (Instance instance : instanceList) {
//			 Instance newInstance = new Instance();
////			 newInstance.setId(instance.getId());
//			 newInstance.setJobName(instance.getJobName());
//			 newInstance.setTaskType(instance.getTaskType());
//			 newInstance.setJobClass(instance.getJobClass());
//			 newInstance.setCronExpression(instance.getCronExpression());
//			 newInstance.setUserId(instance.getUserId());
//			 newInstance.setLocationId(instance.getLocationId());
//			 newInstance.setCameraId(instance.getCameraId());
//			 newInstance.setFileId(instance.getFileId());
//			 newInstance.setCreateTime(instance.getCreateTime());
//			 newInstance.setUpdateTime(instance.getUpdateTime());
//			 newInstance.setIsLatest(1);
//			 newInstance.setIsDelete(instance.getIsDelete());
//			 newInstance.setCatchId(instance.getCatchId());
//			 newInstance.setResultDesc(instance.getResultDesc());
//			 instanceService.insert(newInstance);
//			 System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~new record inserted: " + instance.getId() + "~~~~~~~~~~~~~~~~~~~~~~~~");
//		 }

//	}

}
