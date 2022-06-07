package com.example.demo.hk.dao;

import com.example.demo.hk.entity.Instance;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
public interface InstanceDao {

    String updateQuery = "update quartz_instance_ set is_latest_ = 0 where id_ = #{id}";
    @Update(updateQuery)
    void updateInstance(int id);

    String insertQuery =
            "insert into " + "quartz_instance_ " +
                " (job_name_, job_type_, job_class_, status_, cron_expression_,user_id_, location_id_, camera_id_, file_id_, " +
                    "create_time_, update_time_,is_latest_, is_delete_, catch_id_, result_desc_)" +
                    " values " +
                    "(#{jobName}, #{taskType}, #{jobClass}, #{status}, #{cronExpression}, #{userId}, #{locationId}, #{cameraId},#{fileId}, #{createTime}, #{updateTime}, #{isLatest}, #{isDelete}, #{catchId}, #{resultDesc})";

    @Insert(insertQuery)
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = int.class)
    int insertInstance(Instance instance);

    String selectQuery1 =
            "select id_, job_name_, job_type_, job_class_, status_, cron_expression_,user_id_, location_id_, camera_id_, file_id_, create_time_, update_time_,is_latest_, is_delete_, catch_id_, result_desc_" +
                    " from quartz_instance_" +
                    " where id_ = #{id}";

    @Select(selectQuery1)
    @Results({
            @Result(column="id_", property="id"),
            @Result(column="job_name_", property="jobName"),
            @Result(column="job_type_", property="taskType"),
            @Result(column="job_class_", property="jobClass"),
            @Result(column="status_", property="status"),
            @Result(column="cron_expression_", property="cronExpression"),
            @Result(column="user_id_", property="userId"),
            @Result(column="location_id_", property="locationId"),
            @Result(column="camera_id_", property="cameraId"),
            @Result(column="file_id_", property="fileId"),
            @Result(column="create_time_", property="createTime"),
            @Result(column="update_time_", property="updateTime"),
            @Result(column="is_latest_", property="isLatest"),
            @Result(column="is_delete_", property="isDelete"),
            @Result(column="catch_id_", property="catchId"),
            @Result(column="result_desc_", property="resultDesc")
    })
    List<Instance> getInstanceListById(int id);


    String selectQuery2 =
            "select id_, job_name_, job_type_, job_class_, status_, cron_expression_,user_id_, location_id_, camera_id_, file_id_, create_time_, update_time_,is_latest_, is_delete_, catch_id_, result_desc_" +
                    " from quartz_instance_" +
                    " where status_ = #{status}";
    @Select(selectQuery2)
    @Results({
            @Result(column="id_", property="id"),
            @Result(column="job_name_", property="jobName"),
            @Result(column="job_type_", property="taskType"),
            @Result(column="job_class_", property="jobClass"),
            @Result(column="status_", property="status"),
            @Result(column="cron_expression_", property="cronExpression"),
            @Result(column="user_id_", property="userId"),
            @Result(column="location_id_", property="locationId"),
            @Result(column="camera_id_", property="cameraId"),
            @Result(column="file_id_", property="fileId"),
            @Result(column="create_time_", property="createTime"),
            @Result(column="update_time_", property="updateTime"),
            @Result(column="is_latest_", property="isLatest"),
            @Result(column="is_delete_", property="isDelete"),
            @Result(column="catch_id_", property="catchId"),
            @Result(column="result_desc_", property="resultDesc")
    })
    List<Instance> getInstanceListByStatus(int status);

}
