package com.walkgs.crystolnetwork.offices.manager.permission;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PlayerPermissions {

    private final Player player;

    private final List<PermissionAttachmentInfo> attachments = new ArrayList<>();
    private final Map<String, Boolean> permissions = new LinkedHashMap<>();

    public PlayerPermissions(Player player){
        this.player = player;
    }

    public void setPermission(String permission, boolean status){
        if (!permissions.containsKey(permission)) permissions.put(permission, status); else permissions.replace(permission, status);
    }

    public void addPermission(String permission){
        setPermission(permission, true);
    }

    public void removePermission(String permission){
        setPermission(permission, false);
    }

    public boolean hasPermission(String permission){
        return (permissions.containsKey(permission) ? permissions.get(permission) : false);
    }




}
