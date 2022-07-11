package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.entity.CameraTask;

public interface CameraTaskService {
    CameraTask getCameraTaskByOpts(int locationId, String taskType);
}
