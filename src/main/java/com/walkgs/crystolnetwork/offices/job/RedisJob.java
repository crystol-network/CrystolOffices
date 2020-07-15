package com.walkgs.crystolnetwork.offices.job;

import com.walkgs.crystolnetwork.offices.api.services.OfficesServices;
import com.walkgs.crystolnetwork.offices.services.NetworkService;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisJob extends Thread {

    private final NetworkService networkService;
    private final OfficesServices officesServices;
    private final RedisInboud redisInboud;

    private final Plugin plugin;
    private final Server server;

    public RedisJob(final OfficesServices officesServices) {

        this.officesServices = officesServices;
        this.networkService = officesServices.getNetworkService();
        this.redisInboud = new RedisInboud(officesServices);

        this.plugin = officesServices.getPlugin();
        this.server = plugin.getServer();

    }

    public void run() {
        final JedisPool pool = networkService.getPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.subscribe(redisInboud, officesServices.getChannelName());
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
