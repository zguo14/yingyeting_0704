package com.example.demo.hk.dao;

import com.example.demo.hk.dao.entity.Camera;
import com.example.demo.hk.dao.entity.CameraTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CameraTaskDao {
    String query =
            "select " + "id_, camera_id_, task_type_, channel_, rtsp_cmd_" +
                    " from " + "camera_task_" +
                    " where " + "camera_id_ = #{cameraId} and task_type_ = #{taskType}";
    @Select(query)
    @Results({
            @Result(column="id_", property="id"),
            @Result(column = "camera_id_", property="cameraId"),
            @Result(column="task_type_",property="taskType"),
            @Result(column="channel_" ,property="channel"),
            @Result(column="rtsp_cmd_" ,property="rtspCmd"),

    })
    CameraTask getCameraTaskByOpts(int cameraId, String taskType);


}
