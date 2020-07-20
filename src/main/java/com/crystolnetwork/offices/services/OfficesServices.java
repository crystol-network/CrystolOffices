package com.crystolnetwork.offices.services;

import com.crystolnetwork.offices.OfficesPlugin;
import com.crystolnetwork.offices.annotations.Singleton;
import com.crystolnetwork.offices.entity.PlayerBase;
import com.crystolnetwork.offices.manager.job.jedis.RedisJob;
import com.crystolnetwork.offices.manager.job.mongo.MongoJob;
import com.crystolnetwork.offices.security.SecurityService;
import com.crystolnetwork.offices.services.loaders.GroupLoader;
import com.crystolnetwork.offices.services.loaders.UserLoader;
import com.crystolnetwork.offices.utils.exceptions.CrystolException;
import org.bukkit.plugin.Plugin;

@Singleton()
public class OfficesServices {

    //TODO: FUNCTIONS CLASS

    private final Plugin plugin;
    private final UserLoader userLoader;
    private final GroupLoader groupLoader;
    private final SecurityService securityService;
    private final NetworkService networkService;
    private final PlayerBase playerBase;

    //Connections
    private final RedisJob redisJob;
    private final MongoJob mongoJob;

    private String serverName = "defaultServer";
    private String channelName = "ChannelMessageOf-defaultServer";

    public OfficesServices() {
        plugin = OfficesPlugin.getPlugin(OfficesPlugin.class);

        //initialize the services
        securityService = new SecurityService(plugin.getDataFolder());
        NetworkService networkService = null;
        try{
            networkService = new NetworkService(
                    securityService.getCredential("redis"),
                    securityService.getCredential("mongo")
            );
        } catch (CrystolException e) {
            e.printStackTrace();
        }
        this.networkService = networkService;

        setServerName(securityService.getServerName());

        //Connections
        redisJob = new RedisJob(this);
        mongoJob = new MongoJob(this);

        userLoader = new UserLoader(plugin);
        groupLoader = new GroupLoader(plugin);
        playerBase = new PlayerBase(this);

    }

    public void setServerName(String serverName) {
        this.channelName = "ChannelMessageOf-" + serverName;
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public String getChannelName() {
        return channelName;
    }

    public UserLoader getUserLoader() {
        return userLoader;
    }

    public GroupLoader getGroupLoader() {
        return groupLoader;
    }

    public RedisJob getRedisJob() {
        return redisJob;
    }

    public MongoJob getMongoJob() {
        return mongoJob;
    }

    public PlayerBase getPlayerBase() {
        return playerBase;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public TabService getTabService() {
        return SingletonService.getOrFill(TabService.class);
    }

}
