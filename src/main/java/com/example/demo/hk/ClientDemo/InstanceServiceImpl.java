package com.example.demo.hk.ClientDemo;

import com.example.demo.hk.dao.InstanceDao;
import com.example.demo.hk.entity.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstanceServiceImpl implements InstanceService {
//    @Autowired
//    private InstanceMapper instanceMapper;
    @Autowired
    private InstanceDao instanceDao;
    @Override
    public void update(int id) {
        try {
            instanceDao.updateInstance(id);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("更新失败");
        }

    }

    @Override
    public int insert(Instance instance) {
        try {
            int insertCount = instanceDao.insertInstance(instance);
            if (insertCount > 0) {
                return instance.getId();
//                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("插入失败");
        }
        return -1;
    }

//    @Override
//    public String getTaskTypeByInstanceId(Instance instance) {
//        try {
//            return instanceDao.getTaskTypeByInstanceId(instance);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("查询type失败");
//        }
//        return " ";
//    }
//
//    @Override
//    public int getLocationIdByInstanceId(Instance instance) {
//        try {
//            return instanceDao.getLocationIdByInstanceId(instance);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("查询location失败");
//        }
//        return -1;
//    }

//    @Override
//    public List<Instance> getInstanceListByOpts(int status) {
//        try {
//            return instanceDao.getInstanceListByOpts(status);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("查询instance list失败");
//        }
//        return null;
//    }

    @Override
    public List<Instance> getInstanceById(int id) {
        try {
            List<Instance> l = instanceDao.getInstanceListById(id);
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询instance list by id失败");
        }
        return null;
    }

    @Override
    public List<Instance> getInstanceListByStatus(int status) {
        try {
            List<Instance> l = instanceDao.getInstanceListByStatus(status);
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询instance list by status失败");
        }
        return null;
    }

}
