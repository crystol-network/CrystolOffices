package com.walkgs.crystolnetwork.offices.api.base;

import com.walkgs.crystolnetwork.offices.OfficesPlugin;
import com.walkgs.crystolnetwork.offices.api.PlayerPermission;
import com.walkgs.crystolnetwork.offices.job.RedisJob;
import com.walkgs.crystolnetwork.offices.security.SecurityService;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.NetworkService;
import com.walkgs.crystolnetwork.offices.services.TabService;
import com.walkgs.crystolnetwork.offices.services.UserLoader;
import com.walkgs.crystolnetwork.offices.utils.CachedCycle;
import org.bukkit.plugin.Plugin;

public class ServerOffices {

    //TODO: CYCLE OF USER PERMISSIONS
    private static final CachedCycle.ICycle<ServerOffices> cycle = new CachedCycle(OfficesPlugin.getPlugin(OfficesPlugin.class)).getOrCreate("Permissions");

    public static ServerOffices getInstance() {
        return cycle.getOrComputer(ServerOffices::new);
    }

    public static CachedCycle.ICycle<ServerOffices> getCycle() {
        return cycle;
    }

    //TODO: FUNCTIONS CLASS

    private final Plugin plugin;
    private final UserLoader userLoader;
    private final GroupLoader groupLoader;
    private final SecurityService securityService;
    private final NetworkService networkService;
    private final RedisJob redisJob;
    private final PlayerPermission playerPermission;

    private String serverName = "defaultServer";
    private String channelName = "ChannelMessageOf-defaultServer";

    protected ServerOffices() {
        plugin = OfficesPlugin.getPlugin(OfficesPlugin.class);
        userLoader = new UserLoader(plugin);
        groupLoader = new GroupLoader(plugin);

        //initialize the services
        securityService = new SecurityService(plugin.getDataFolder());
        networkService = new NetworkService(securityService.getCredential("redis"));

        redisJob = new RedisJob(this);
        playerPermission = new PlayerPermission(this);
    }

    //public UserManager getUser(Player player) {
    //   return getUser(player.getUniqueId());
    //}

    //public UserManager getUser(UUID uuid) {
    //   return userLoader.get(uuid);
    //}

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

    public PlayerPermission getPlayerPermission() {
        return playerPermission;
    }

    //public void loadUser(final OfflinePlayer player) {
    //    loadUser(player.getUniqueId());
    //}

    //public void loadUser(final UUID uuid) {

    //    userLoader.putIfNotExists(uuid, new UserManager(plugin, uuid));
    //    getUser(uuid).addGroups(
    //            getGroupLoader().getDefaultGroups()
    //    );

    //}

    //public void injectInUser(final Player player) {

    //    final PermissibleBase permissibleBase = PermissibleInjector.getPermissible(player);
    //    if (permissibleBase != null) {

    //        final CrystolPermissible newPermissibleBase = new CrystolPermissible(player);
    //        newPermissibleBase.setOldPermissibleBase(permissibleBase);

    //        final PlayerInjectPermissibleEvent permissibleEvent = new PlayerInjectPermissibleEvent(player, newPermissibleBase, permissibleBase).call();
    //        if (!permissibleEvent.isCancelled()) {
    //           PermissibleInjector.inject(player, permissibleEvent.getNewPermissible());
    //        }
    //    }

    //}

    public TabService getTabService() {
        return TabService.getInstance();
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

}
