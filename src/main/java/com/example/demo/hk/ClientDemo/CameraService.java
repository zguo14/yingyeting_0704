package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.entity.Camera;

import java.util.List;

public interface CameraService {
    Camera getCameraById(int cameraId);
    List<Camera> getCameraList();
    void updateStatus(int cameraId, String status);
}
