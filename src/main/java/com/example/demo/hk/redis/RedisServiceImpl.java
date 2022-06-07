package com.example.demo.hk.redis;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.util.Pool;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class RedisServiceImpl implements RedisService {

    private Pool<Jedis> jedisPool;

    @Override
    public void init() {
        jedisPool = new JedisPool( "172.18.82.41", 6379);
    }

    @Override
    public StreamEntryID xadd(String key, StreamEntryID streamEntryID, Map<String, String> content) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xadd(key, streamEntryID, content);
        }
    }

    @Override
    public String xgroupCreate(String stream, String group, Boolean makeStream) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xgroupCreate(stream, group, null, makeStream);
        }
    }

    @Override
    public List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xrevrange(key, end, start, count);
        }
    }

    @Override
    public List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end, int count) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xrange(key, start, end, count);
        }
    }

    @Override
    public List<Map.Entry<String, List<StreamEntry>>> xreadGroup(String group, String consumer, int count, Map.Entry<String, StreamEntryID>... streams) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xreadGroup(group, consumer, count, 0, false, streams);
        }
    }

    @Override
    public List<Map.Entry<String, List<StreamEntry>>> xread(int count, Map.Entry<String, StreamEntryID>... streams) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.xread(count, 0, streams);
        }
    }
}
