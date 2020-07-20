package com.crystolnetwork.offices.services;

import com.crystolnetwork.offices.security.SecurityObject;
import com.crystolnetwork.offices.utils.exceptions.CrystolException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class NetworkService {

    private final MongoClient mongoPool;
    private final JedisPool jedisPool;
    private final SecurityObject jedisCredentials;
    private final SecurityObject mongoCredentials;

    public NetworkService(final SecurityObject... credentials) throws CrystolException {

        jedisCredentials = credentials[0];
        mongoCredentials = credentials[1];

        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        try {
            this.jedisPool = new JedisPool(
                    jedisPoolConfig,
                    jedisCredentials.getHost(),
                    jedisCredentials.getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    jedisCredentials.getPassword()
            );
        } catch (Exception e){
            throw new CrystolException("Attempt to connect to 'JEDIS' failed.", NetworkService.class);
        }

        try {
            this.mongoPool = MongoClients.create(this.mongoCredentials.getUri());
        } catch (Exception exception) {
            throw new CrystolException("Attempt to connect to 'MONGO' failed.", NetworkService.class);
        }

        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

    }

    public SecurityObject getMongoCredentials() {
        return mongoCredentials;
    }

    public SecurityObject getJedisCredentials() {
        return jedisCredentials;
    }

    public MongoClient getMongoPool() {
        return mongoPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void closeJedisPool() {
        jedisPool.close();
    }

    public void closeMongoPool() {
        mongoPool.close();
    }

}
