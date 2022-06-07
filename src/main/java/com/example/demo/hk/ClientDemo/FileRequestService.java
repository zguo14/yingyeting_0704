package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.entity.FileRequest;
import com.example.demo.hk.entity.Instance;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FileRequestService {
    List<FileRequest> getFileRequestListByOpts(int status);
}
