package com.walkgs.crystolnetwork.offices.inject;

import com.walkgs.crystolnetwork.offices.api.PlayerBase;
import com.walkgs.crystolnetwork.offices.api.PlayerPermission;
import com.walkgs.crystolnetwork.offices.api.services.OfficesServices;
import com.walkgs.crystolnetwork.offices.services.GroupService;
import com.walkgs.crystolnetwork.offices.services.classlife.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

public final class CrystolPermissible extends PermissibleBase {

    private final Player player;

    private final OfficesServices officesServices = Singleton.getOrFill(OfficesServices.class);
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
        final GroupService groupService = playerBase.getUser(player).getLargestGroup();
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
