package com.walkgs.crystolnetwork.offices.api.services;

import com.walkgs.crystolnetwork.offices.OfficesPlugin;
import com.walkgs.crystolnetwork.offices.api.PlayerBase;
import com.walkgs.crystolnetwork.offices.job.RedisJob;
import com.walkgs.crystolnetwork.offices.security.SecurityService;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.NetworkService;
import com.walkgs.crystolnetwork.offices.services.TabService;
import com.walkgs.crystolnetwork.offices.services.UserLoader;
import com.walkgs.crystolnetwork.offices.services.classlife.Singleton;
import com.walkgs.crystolnetwork.offices.services.classlife.annotation.ClassLife;
import org.bukkit.plugin.Plugin;

@ClassLife()
public class OfficesServices {

    //TODO: FUNCTIONS CLASS

    private final Plugin plugin;
    private final UserLoader userLoader;
    private final GroupLoader groupLoader;
    private final SecurityService securityService;
    private final NetworkService networkService;
    private final RedisJob redisJob;
    private final PlayerBase playerBase;

    private String serverName = "defaultServer";
    private String channelName = "ChannelMessageOf-defaultServer";

    public OfficesServices() {
        plugin = OfficesPlugin.getPlugin(OfficesPlugin.class);
        userLoader = new UserLoader(plugin);
        groupLoader = new GroupLoader(plugin);

        //initialize the services
        securityService = new SecurityService(plugin.getDataFolder());
        networkService = new NetworkService(securityService.getCredential("redis"));

        redisJob = new RedisJob(this);
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

    public PlayerBase getPlayerBase() { return playerBase; }

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
        return Singleton.getOrFill(TabService.class);
    }

}
