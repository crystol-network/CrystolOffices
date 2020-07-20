package com.crystolnetwork.offices.services.loaders;

import com.crystolnetwork.offices.manager.UserData;
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

    private final Map<UUID, UserData> permissionsCache = new HashMap<>();

    public UserData get(final Player player) {
        return get(player.getUniqueId());
    }

    public UserData get(final UUID uuid) {
        return permissionsCache.get(uuid);
    }

    public boolean hasLoaded(final UUID uuid) {
        return permissionsCache.containsKey(uuid);
    }

    public boolean loadIfNotLoaded(final UUID uuid, final UserData userData) {
        if (hasLoaded(uuid)) return false;
        permissionsCache.put(uuid, userData);
        return true;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
