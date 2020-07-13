package com.walkgs.crystolnetwork.offices.services;

import com.walkgs.crystolnetwork.offices.security.SecurityObject;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public final class NetworkService {

    private final JedisPool pool;

    public NetworkService(SecurityObject credential) {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();

        this.pool = new JedisPool(
                poolConfig,
                credential.getHost(),
                credential.getPort(),
                Protocol.DEFAULT_TIMEOUT,
                credential.getPassword()
        );
    }

    public JedisPool getPool() {
        return pool;
    }

    public void closePool() {
        pool.close();
    }
}
