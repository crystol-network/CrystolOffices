package com.crystolnetwork.offices.manager.job.jedis;

import com.crystolnetwork.offices.services.NetworkService;
import com.crystolnetwork.offices.services.OfficesServices;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisJob extends Thread {

    private final NetworkService networkService;
    private final OfficesServices officesServices;
    private final RedisInboud redisInboud;

    public RedisJob(final OfficesServices officesServices) {

        this.officesServices = officesServices;
        this.networkService = officesServices.getNetworkService();
        this.redisInboud = new RedisInboud(officesServices);

    }

    public void run() {
        final JedisPool pool = networkService.getJedisPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.subscribe(redisInboud, officesServices.getChannelName());
        }
    }

    public OfficesServices getOfficesServices() {
        return officesServices;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

}
