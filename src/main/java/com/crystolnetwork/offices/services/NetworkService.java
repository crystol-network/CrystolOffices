package com.crystolnetwork.offices.services;

import com.crystolnetwork.offices.security.SecurityObject;
import com.crystolnetwork.offices.security.SecurityService;
import com.crystolnetwork.offices.services.network.DataConnection;
import com.crystolnetwork.offices.services.network.data.DataConnectionType;
import com.crystolnetwork.offices.utils.exceptions.CrystolException;
import dev.king.universal.api.JdbcProvider;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class NetworkService {

    private final JedisPool jedisPool;
    private final DataConnection dataConnection = SingletonService.getOrFill(DataConnection.class);
    private final SecurityObject jedisCredentials;
    private final SecurityObject databaseCredentials;

    public NetworkService(final SecurityObject... credentials) throws CrystolException {

        jedisCredentials = credentials[0];
        databaseCredentials = credentials[1];

        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        try {
            this.jedisPool = new JedisPool(
                    jedisPoolConfig,
                    jedisCredentials.getHost(),
                    jedisCredentials.getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    jedisCredentials.getPassword()
            );
        } catch (Exception e) {
            throw new CrystolException("Attempt to connect to 'JEDIS' failed.", NetworkService.class);
        }

        if (!dataConnection.build(databaseCredentials)) {
            throw new CrystolException("Attempt to connect to '" + databaseCredentials.getType() + "' failed.", NetworkService.class);
        }

        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

    }

    public DataConnection getDataConnection() {
        return dataConnection;
    }

    public SecurityObject getJedisCredentials() {
        return jedisCredentials;
    }

    public SecurityObject getDatabaseCredentials() {
        return databaseCredentials;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void closeJedisPool() {
        jedisPool.close();
    }

}
