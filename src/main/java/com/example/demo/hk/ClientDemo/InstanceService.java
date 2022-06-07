package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.entity.Instance;
import org.springframework.stereotype.Service;

import java.util.List;

public interface InstanceService {
    void update(int id);
    int insert(Instance instance);
    List<Instance> getInstanceById(int id);
    List<Instance> getInstanceListByStatus(int status);
}
