package com.walkgs.crystolnetwork.offices;

import com.walkgs.crystolnetwork.offices.api.UserPermission;
import com.walkgs.crystolnetwork.offices.listeners.JoinListener;
import com.walkgs.crystolnetwork.offices.utils.CachedCycle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OfficesPlugin extends JavaPlugin {

    //TODO: CYCLE OF USER PERMISSIONS
    private static final CachedCycle.ICycle<UserPermission> cycle = new CachedCycle(OfficesPlugin.getPlugin(OfficesPlugin.class)).getOrCreate("Permissions");

    public static UserPermission getInstance() {
        return cycle.getOrComputer(() -> new UserPermission());
    }


    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

    }
}
