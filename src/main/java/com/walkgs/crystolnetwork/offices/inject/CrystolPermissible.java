package com.walkgs.crystolnetwork.offices.inject;

import com.walkgs.crystolnetwork.offices.api.ServerOffices;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public final class CrystolPermissible extends PermissibleBase {

    private final Player player;

    private final ServerOffices serverOffices = ServerOffices.getInstance();

    private PermissibleBase oldPermissibleBase;

    public CrystolPermissible(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        if (isOp())
            return true;
        GroupPermission groupPermission = serverOffices.getUser(player).getLargestGroup();
        if (groupPermission != null)
            return groupPermission.hasPermission(permission);
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return hasPermission(perm.getName());
    }

    public void setOldPermissibleBase(PermissibleBase permissibleBase) {
        oldPermissibleBase = permissibleBase;
    }

    public PermissibleBase getOldPermissibleBase() {
        return oldPermissibleBase;
    }

    public Player getPlayer() {
        return player;
    }
}
