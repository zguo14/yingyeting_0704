package com.example.demo.hk.dao;

import com.example.demo.hk.dao.entity.FileRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface FileRequestDao {
    String query =
            "select " + "id_, job_name_, job_type_, job_class_, status_," +
    "cron_expression_, location_id_, user_id_," +
    "video_interval_hours_, video_interval_minutes_, video_interval_seconds_," +
    "begin_hours_, begin_minutes_, begin_seconds_" +
    " from " + "quartz_file_requests_" +
    " where " + "status_ = #{status}";
    @Select(query)
    @Results({
            @Result(column="id_", property="Id"),
            @Result(column="job_name_",property="jobName"),
            @Result(column="job_type_" ,property="jobType"),
            @Result(column="job_class_" ,property="jobClass"),
            @Result(column="status_" ,property="status"),
            @Result(column="cron_expression_" ,property="cronExpression"),
            @Result(column="location_id_" ,property="locationId"),
            @Result(column="user_id_" ,property="userId"),
            @Result(column="video_interval_hours_" ,property="intervalH"),
            @Result(column="video_interval_minutes_" ,property="intervalM"),
            @Result(column="video_interval_seconds_" ,property="intervalS"),
            @Result(column="begin_hours_" ,property="beginH"),
            @Result(column="begin_minutes_" ,property="beginM"),
            @Result(column="begin_seconds_" ,property="beginS")
    })
    List<FileRequest> getFileRequestByOpts(int status);


}
