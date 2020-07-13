package com.walkgs.crystolnetwork.offices.services;

import com.walkgs.crystolnetwork.offices.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserLoader {

    private final Plugin plugin;

    public UserLoader(final Plugin plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, UserManager> permissionsCache = new HashMap<>();

    public UserManager get(final Player player) {
        return get(player.getUniqueId());
    }

    public UserManager get(final UUID uuid) {
        return permissionsCache.get(uuid);
    }

    public boolean hasLoaded(final UUID uuid) {
        return permissionsCache.containsKey(uuid);
    }

    public boolean loadIfNotLoaded(final UUID uuid, final UserManager userManager) {
        if (hasLoaded(uuid)) return false;
        permissionsCache.put(uuid, userManager);
        return true;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
