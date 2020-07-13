package com.walkgs.crystolnetwork.offices.inject;

import com.walkgs.crystolnetwork.offices.api.ServerOffices;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public final class CrystolPermissible extends PermissibleBase {

    private final Player player;

    private final ServerOffices userPermission = ServerOffices.getInstance();

    private PermissibleBase oldPermissibleBase;

    public CrystolPermissible(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        boolean has = isOp();
        if (!has) {
            GroupPermission groupPermission = userPermission.getUser(player).getLargestGroup();
            if (groupPermission != null)
                has = groupPermission.hasPermission(permission);
        }
        return has;
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
