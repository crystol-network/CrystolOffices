package com.walkgs.crystolnetwork.offices.job;

import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.services.NetworkService;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisJob extends Thread {

    private final NetworkService networkService;
    private final ServerOffices serverOffices;
    private final RedisInboud redisInboud;

    private final Plugin plugin;
    private final Server server;

    public RedisJob(final ServerOffices serverOffices) {

        this.serverOffices = serverOffices;
        this.networkService = serverOffices.getNetworkService();
        this.redisInboud = new RedisInboud(serverOffices);

        this.plugin = serverOffices.getPlugin();
        this.server = plugin.getServer();

    }

    public void run() {
        final JedisPool pool = networkService.getPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.subscribe(redisInboud, serverOffices.getChannelName());
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Server getServer() {
        return server;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

}
