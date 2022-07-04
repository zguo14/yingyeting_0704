package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.CameraDao;
import com.example.demo.hk.dao.entity.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            System.out.println("查询camera失败");
        }
        return null;
    }
}
