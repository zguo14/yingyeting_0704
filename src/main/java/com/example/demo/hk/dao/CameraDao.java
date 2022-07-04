package com.example.demo.hk.dao;

import com.example.demo.hk.dao.entity.Camera;
import com.example.demo.hk.dao.entity.FileRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface CameraDao {

    String query =
            "select " + "id_, ip_, location_id_, name_, state_, task_type_, port_" +
                    " from " + "camera_" +
                    " where " + "location_id_ = #{locationId}";
    @Select(query)
    @Results({
            @Result(column="id_", property="cameraId"),
            @Result(column = "ip_", property="cameraIp"),
            @Result(column="location_id_",property="locationId"),
            @Result(column="name_" ,property="cameraName"),
            @Result(column="state_" ,property="state"),
            @Result(column="task_type_" ,property="taskType"),
            @Result(column="port_" ,property="cameraPort"),

    })
    Camera getCameraInfoByLocation(int locationId);
}
