package com.crystolnetwork.offices.manager.job.jedis;

import com.crystolnetwork.offices.services.NetworkService;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.LinkedHashMap;
import java.util.Map;

public class RedisSender {

    private final JedisPool jedisPool;
    private final Gson gson = new Gson();

    private final Map<Integer, String> values = new LinkedHashMap<>();

    private int lastIndex = 0;

    public RedisSender(NetworkService networkService) {
        this.jedisPool = networkService.getJedisPool();
    }

    public void add(String message) {
        add(lastIndex++, message);
    }

    public void add(int index, String message) {
        if (!values.containsKey(index))
            values.put(index, message);
    }

    public void removeLastAdded() {
        remove(lastIndex--);
    }

    public void remove(int index) {
        if (values.containsKey(index))
            values.remove(index);
    }

    public void replace(int index, String message) {
        if (values.containsKey(index))
            values.replace(index, message);
    }

    public void clear() {
        values.clear();
    }

    public void send(String serverName) {
        final String message = gson.toJson(values);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.publish("ChannelMessageOf-" + serverName, message);
        }
    }

}
