package com.walkgs.crystolnetwork.offices.api;

import com.walkgs.crystolnetwork.offices.OfficesPlugin;
import com.walkgs.crystolnetwork.offices.manager.permission.PlayerPermissions;
import com.walkgs.crystolnetwork.offices.utils.CachedCycle;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class UserPermission {

    //TODO: CYCLE OF USER PERMISSIONS
    private static final CachedCycle.ICycle<UserPermission> cycle = new CachedCycle(OfficesPlugin.getPlugin(OfficesPlugin.class)).getOrCreate("Permissions");

    public static UserPermission getInstance() {
        return cycle.getOrComputer(() -> new UserPermission());
    }

    //TODO: FUNCTIONS CLASS

    private final Map<UUID, PlayerPermissions> permissionsCache = new LinkedHashMap<>();

    public void insetUser(Player player, PlayerPermissions playerPermissions){
        if (!permissionsCache.containsKey(player.getUniqueId()))
            permissionsCache.put(player.getUniqueId(), playerPermissions);
        if (permissionsCache.containsKey(player.getUniqueId()))
            player.sendMessage("PUTTED");
    }

    public boolean setPermission(Player player, String permission, boolean status){
        boolean sucess = false;
        if (permissionsCache.containsKey(player.getUniqueId())){
            permissionsCache.get(player.getUniqueId()).setPermission(permission, status);
            sucess = true;
        }
        return sucess;
    }

    public boolean addPermission(Player player, String permission) {
        return setPermission(player, permission, true);
    }

    public boolean hasPermission(Player player, String permission) {
        boolean sucess = false;
        if (permissionsCache.containsKey(player.getUniqueId()))
            sucess = permissionsCache.get(player.getUniqueId()).hasPermission(permission);
        return sucess;
    }

}
