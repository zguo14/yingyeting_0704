package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.CameraDao;
import com.example.demo.hk.dao.CameraTaskDao;
import com.example.demo.hk.dao.entity.Camera;
import com.example.demo.hk.dao.entity.CameraTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CameraTaskServiceImpl implements CameraTaskService {

    @Autowired
    private CameraTaskDao cameraTaskDao;
    @Override
    public CameraTask getCameraTaskByOpts(int cameraId, String taskType) {

        try {
            return cameraTaskDao.getCameraTaskByOpts(cameraId, taskType);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询camera task失败");
        }
        return null;
    }

}
