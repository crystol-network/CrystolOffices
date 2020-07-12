package com.walkgs.crystolnetwork.offices.listeners;

import com.walkgs.crystolnetwork.offices.api.UserPermission;
import com.walkgs.crystolnetwork.offices.events.PlayerInjectPermissibleEvent;
import com.walkgs.crystolnetwork.offices.inject.CrystolPermissible;
import com.walkgs.crystolnetwork.offices.inject.PermissibleInjector;
import com.walkgs.crystolnetwork.offices.manager.permission.PlayerPermissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissibleBase;

public final class JoinListener implements Listener {

    @EventHandler
    public final void onJoin(final PlayerJoinEvent event){

        final Player player = event.getPlayer();

        //TODO GET DEFAULT PERMISSIBLE AND CALL EVENT
        final PermissibleBase permissibleBase = PermissibleInjector.getPermissible(player);
        if (permissibleBase != null) {

            final CrystolPermissible newPermissibleBase = new CrystolPermissible(player);
            newPermissibleBase.setOldPermissibleBase(permissibleBase);

            final PlayerInjectPermissibleEvent permissibleEvent = new PlayerInjectPermissibleEvent(player, newPermissibleBase, permissibleBase).call();
            if (!permissibleEvent.isCancelled()){
                //TODO: INJECT NEW PERMISSIBLE
                PermissibleInjector.inject(player, permissibleEvent.getNewPermissible());
            }
        }

        UserPermission.getInstance().insetUser(player, new PlayerPermissions(player));
        UserPermission.getInstance().addPermission(player, "economy.viewmoney");
        if (player.hasPermission("economy.viewmoney")){
            player.sendMessage("FUNCIONOU PORRA");
        } else {
            player.sendMessage("DEU ERRADO PORRA");
        }

        if (UserPermission.getInstance().hasPermission(player, "economy.viewmoney")){
            player.sendMessage("FUNCIONOU PORRA 2");
        } else {
            player.sendMessage("DEU ERRADO PORRA 2");
        }

    }

}
