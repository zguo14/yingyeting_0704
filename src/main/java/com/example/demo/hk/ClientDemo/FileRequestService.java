package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.entity.FileRequest;

import java.util.List;


public interface FileRequestService {
    List<FileRequest> getFileRequestListByOpts(int status);
}
