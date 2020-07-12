package com.walkgs.crystolnetwork.offices.inject;

import com.walkgs.crystolnetwork.offices.api.UserPermission;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public final class CrystolPermissible extends PermissibleBase {

    private final Player player;

    private PermissibleBase oldPermissibleBase;

    public CrystolPermissible(Player player) {

        super(player);

        this.player = player;

    }

    @Override
    public boolean hasPermission(String permission) {
        return UserPermission.getInstance().hasPermission(player, permission);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return hasPermission(perm.getName());
    }

    public void setOldPermissibleBase(PermissibleBase permissibleBase) { oldPermissibleBase = permissibleBase; }
    public PermissibleBase getOldPermissibleBase() { return oldPermissibleBase; }

}
