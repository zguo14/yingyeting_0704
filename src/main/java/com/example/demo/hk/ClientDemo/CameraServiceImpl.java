package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.CameraDao;
import com.example.demo.hk.dao.entity.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraServiceImpl implements CameraService {
    @Autowired
    private CameraDao cameraDao;
    @Override
    public Camera getCameraById(int locationId) {

        try {
            return cameraDao.getCameraInfoByLocation(locationId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询camera info失败");
        }
        return null;
    }

    @Override
    public List<Camera> getCameraList() {
        try {
            return cameraDao.getCameraList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询camera list失败");
        }
        return null;
    }

    @Override
    public void updateStatus(int cameraId, String status) {
        try {
            cameraDao.updateStatus(cameraId, status);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("更新camera status失败");
        }
    }
}
