package com.example.demo.hk.redis;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.util.List;
import java.util.Map;

public interface RedisService {
    void init();
    /**
     *  添加消息
     * @param key
     * @param streamEntryID
     * @param content
     * @return
     */
    StreamEntryID xadd(String key, StreamEntryID streamEntryID, Map<String, String> content);


    /**
     * 创建分组
     * @param stream
     * @param group
     * @param makeStream
     * @return
     */
    String xgroupCreate(String stream, String group, Boolean makeStream);


    /**
     * 倒序获取历史消息
     * @param key
     * @param end
     * @param start
     * @param count
     * @return
     */
    List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count);

    /**
     * 正序获取历史消息
     * @param key
     * @param start
     * @param end
     * @param count
     * @return
     */
    List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end, int count);

    /**
     * 按分组获取消息
     * @param group
     * @param consumer
     * @param count
     * @param streams
     * @return
     */
    List<Map.Entry<String, List<StreamEntry>>> xreadGroup(String group, String consumer, int count, Map.Entry<String, StreamEntryID>... streams);


    /**
     * 获取消息
     * @param count  获取数据
     * @param streams 起始消息ID
     * @return
     */
    List<Map.Entry<String, List<StreamEntry>>> xread(int count, Map.Entry<String, StreamEntryID>... streams);
}
