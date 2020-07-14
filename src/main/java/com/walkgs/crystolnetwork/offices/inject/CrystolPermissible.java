package com.walkgs.crystolnetwork.offices.inject;

import com.walkgs.crystolnetwork.offices.api.PlayerPermission;
import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public final class CrystolPermissible extends PermissibleBase {

    private final Player player;

    private final PlayerPermission playerPermission;
    private final ServerOffices serverOffices;

    private PermissibleBase oldPermissibleBase;

    public CrystolPermissible(Player player) {
        super(player);
        this.player = player;
        this.serverOffices = ServerOffices.getInstance();
        this.playerPermission = serverOffices.getPlayerPermission();
    }

    @Override
    public boolean hasPermission(String permission) {
        if (isOp())
            return true;
        final GroupPermission groupPermission = playerPermission.getUser(player).getLargestGroup();
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
