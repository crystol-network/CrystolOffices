package com.crystolnetwork.offices.utils.inject;

import com.crystolnetwork.offices.entity.PlayerBase;
import com.crystolnetwork.offices.manager.Group;
import com.crystolnetwork.offices.services.OfficesServices;
import com.crystolnetwork.offices.services.SingletonService;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public final class CrystolPermissible extends PermissibleBase {

    private final Player player;

    private final OfficesServices officesServices = SingletonService.getOrFill(OfficesServices.class);
    private final PlayerBase playerBase = officesServices.getPlayerBase();

    private PermissibleBase oldPermissibleBase;

    public CrystolPermissible(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        if (isOp())
            return true;
        final Group groupService = playerBase.getUser(player).getLargestGroup();
        if (groupService != null)
            return groupService.hasPermission(permission);
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
