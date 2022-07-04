package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.FileRequestDao;
import com.example.demo.hk.dao.entity.FileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileRequestServiceImpl implements FileRequestService {
//    @Autowired
//    private FileRequestMapper fileRequestMapper;
    @Autowired
    private FileRequestDao fileRequestDao;

    @Override
    public List<FileRequest> getFileRequestListByOpts(int status) {
        try {
//            return fileRequestMapper.getFileRequestListByOpts(status);
            return fileRequestDao.getFileRequestByOpts(status);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询filerequest list失败");
        }
        return null;

    }
}
