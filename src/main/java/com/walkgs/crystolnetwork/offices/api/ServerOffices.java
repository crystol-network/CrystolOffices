package com.walkgs.crystolnetwork.offices.api;

import com.walkgs.crystolnetwork.offices.OfficesPlugin;
import com.walkgs.crystolnetwork.offices.events.PlayerInjectPermissibleEvent;
import com.walkgs.crystolnetwork.offices.inject.CrystolPermissible;
import com.walkgs.crystolnetwork.offices.inject.PermissibleInjector;
import com.walkgs.crystolnetwork.offices.manager.UserManager;
import com.walkgs.crystolnetwork.offices.services.GroupLoader;
import com.walkgs.crystolnetwork.offices.services.TabService;
import com.walkgs.crystolnetwork.offices.services.UserLoader;
import com.walkgs.crystolnetwork.offices.utils.CachedCycle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

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

    private String serverName;

    protected ServerOffices() {
        plugin = OfficesPlugin.getPlugin(OfficesPlugin.class);
        userLoader = new UserLoader(plugin);
        groupLoader = new GroupLoader(plugin);
        Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] §aServer services started.");
    }

    public UserManager getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public UserManager getUser(UUID uuid) {
        return userLoader.get(uuid);
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public UserLoader getUserLoader() {
        return userLoader;
    }

    public GroupLoader getGroupLoader() {
        return groupLoader;
    }

    public void loadUser(final Player player) {

        //TODO: LOAD PLAYER DATA
        userLoader.putIfNotExists(player, new UserManager(plugin, player));

        //TODO: SET PLAYER DEFAULT GROUP
        getUser(player)
                .addGroups(getGroupLoader().getDefaultGroups());

        //TODO: GET DEFAULT PERMISSIBLE AND CALL EVENT
        final PermissibleBase permissibleBase = PermissibleInjector.getPermissible(player);
        if (permissibleBase != null) {

            final CrystolPermissible newPermissibleBase = new CrystolPermissible(player);
            newPermissibleBase.setOldPermissibleBase(permissibleBase);

            final PlayerInjectPermissibleEvent permissibleEvent = new PlayerInjectPermissibleEvent(player, newPermissibleBase, permissibleBase).call();
            if (!permissibleEvent.isCancelled()) {
                //TODO: INJECT NEW PERMISSIBLE
                PermissibleInjector.inject(player, permissibleEvent.getNewPermissible());
            }
        }

    }

    public TabService getTabService() {
        return TabService.getInstance();
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
